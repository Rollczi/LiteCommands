package dev.rollczi.litecommands.annotations.command.executor;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.command.requirement.RequirementMatch;
import dev.rollczi.litecommands.command.requirement.RequirementsResult;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER, Requirement<SENDER, ?>> {

    private final Method method;
    private final Object instance;
    private final MethodDefinition<SENDER> definition;

    MethodCommandExecutor(
        CommandRoute<SENDER> parent,
        Method method,
        Object instance,
        MethodDefinition<SENDER> definition) {
        super(parent, definition.getRequirements());
        this.method = method;
        this.instance = instance;
        this.definition = definition;
    }

    @Override
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> results) {
        Object[] objects = new Object[method.getParameterCount()];

        for (int parameterIndex = 0; parameterIndex < method.getParameterCount(); parameterIndex++) {
            Requirement<SENDER, ?> requirement = definition.getRequirement(parameterIndex);
            String name = requirement.getName();

            if (!results.has(name)) {
                return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved, missing " + name));
            }

            RequirementMatch<SENDER, ?, ?> requirementMatch = results.get(name);
            Object unwrapped = requirementMatch.getResult().unwrap();

            objects[parameterIndex] = unwrapped;
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
