package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.command.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.command.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.requirement.RequirementMatch;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER, ParameterRequirement<SENDER, ?>> {

    private final Method method;
    private final Object instance;
    private final Class<?> returnType;

    MethodCommandExecutor(
        Method method,
        Object instance,
        List<ParameterRequirement<SENDER, ?>> preparedArguments
    ) {
        super(preparedArguments);
        this.method = method;
        this.instance = instance;
        this.returnType = method.getReturnType();
    }

    @Override
    public CommandExecutorMatchResult match(List<RequirementMatch<SENDER, ParameterRequirement<SENDER, ?>, Object>> results) {
        if (results.size() != this.method.getParameterCount()) {
            return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved"));
        }

        Object[] objects = results.stream()
            .sorted(Comparator.comparingInt(pair -> pair.getRequirement().getParameterIndex()))
            .map(successMatch -> successMatch.getResult())
            .map(wrap -> wrap.unwrap())
            .toArray();

        return CommandExecutorMatchResult.success(() -> {
            try {
                this.method.setAccessible(true);

                return CommandExecuteResult.success(this.method.invoke(this.instance, objects));
            }
            catch (IllegalAccessException exception) {
                throw new LiteCommandsReflectException(this.method, "Cannot access method", exception);
            }
            catch (InvocationTargetException exception) {
                Throwable targetException = exception.getTargetException();

                throw new LiteCommandsReflectException(this.method, "Command method threw " + targetException.getClass().getSimpleName(), targetException);
            }
        });
    }

}
