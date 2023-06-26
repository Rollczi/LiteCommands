package dev.rollczi.litecommands.annotations.command.executor;

import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirement;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.requirement.RequirementMatch;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER, ParameterRequirement<SENDER, ?>> {

    private final Method method;
    private final Object instance;

    MethodCommandExecutor(
        CommandRoute<SENDER> parent,
        Method method,
        Object instance,
        List<ParameterRequirement<SENDER, ?>> preparedArguments
    ) {
        super(parent, preparedArguments);
        this.method = method;
        this.instance = instance;
    }

    @Override
    public CommandExecutorMatchResult match(List<RequirementMatch<SENDER, ParameterRequirement<SENDER, ?>, Object>> results) {
        if (results.size() != this.method.getParameterCount()) {
            return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved"));
        }

        Object[] objects = new Object[results.size()];

        for (RequirementMatch<SENDER, ParameterRequirement<SENDER, ?>, Object> result : results) {
            objects[result.getRequirement().getParameterIndex()] = result.getResult().unwrap();
        }

        return CommandExecutorMatchResult.success(() -> {
            try {
                this.method.setAccessible(true);

                return CommandExecuteResult.success(this, this.method.invoke(this.instance, objects));
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
