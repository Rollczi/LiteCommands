package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.command.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;
import dev.rollczi.litecommands.wrapper.Wrapped;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER, ParameterCommandRequirement<SENDER, ?>> {

    private final Method method;
    private final Object instance;
    private final Class<?> returnType;

    MethodCommandExecutor(
        Method method,
        Object instance,
        List<ParameterCommandRequirement<SENDER, ?>> preparedArguments
    ) {
        super(preparedArguments);
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
    }

    @Override
    protected CommandExecutorMatchResult match(List<Match<ParameterCommandRequirement<SENDER, ?>>> results) {
        if (results.size() != this.method.getParameterCount()) {
            return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved"));
        }

        Object[] objects = results.stream()
            .sorted(Comparator.comparingInt(pair -> pair.getArgument().getParameterIndex()))
            .map(pair -> pair.getResult())
            .map(success -> success.getSuccess())
            .map(Wrapped::unwrap)
            .toArray();

        return CommandExecutorMatchResult.success(() -> {
            try {
                this.method.setAccessible(true);

                return CommandExecuteResult.success(this.method.invoke(this.instance, objects), this.returnType);
            }
            catch (Exception exception) {
                return CommandExecuteResult.failed(exception);
            }
        });
    }

}
