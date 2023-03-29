package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.argument.PreparedArgumentResult;
import dev.rollczi.litecommands.command.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import panda.std.Pair;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER, ParameterPreparedArgument<SENDER, ?>> {

    private final Method method;
    private final Object instance;
    private final Class<?> returnType;

    MethodCommandExecutor(
        Method method,
        Object instance,
        List<ParameterPreparedArgument<SENDER, ?>> preparedArguments
    ) {
        super(preparedArguments);
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
    }

    @Override
    protected CommandExecutorMatchResult match(List<Pair<ParameterPreparedArgument<SENDER, ?>, PreparedArgumentResult.Success<?>>> results) {
        if (results.size() != this.method.getParameterCount()) {
            return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved"));
        }

        Object[] objects = results.stream()
            .sorted(Comparator.comparingInt(pair -> pair.getFirst().getParameterIndex()))
            .map(pair -> pair.getSecond())
            .map(success -> success.getWrappedExpected())
            .map(Supplier::get)
            .map(WrappedExpected::unwrap)
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
