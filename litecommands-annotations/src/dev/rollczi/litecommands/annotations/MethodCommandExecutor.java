package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementMatch;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER> {

    private final Method method;
    private final Object instance;
    private final MethodDefinition definition;

    MethodCommandExecutor(
        CommandRoute<SENDER> parent,
        Method method,
        Object instance,
        MethodDefinition definition,
        Meta meta
    ) {
        super(parent, definition.getArguments(), definition.getContextRequirements(), definition.getBindRequirements());
        this.method = method;
        this.instance = instance;
        this.definition = definition;
        this.meta.apply(meta);
    }

    @Override
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> results) {
        Object[] objects = new Object[method.getParameterCount()];

        for (int parameterIndex = 0; parameterIndex < method.getParameterCount(); parameterIndex++) {
            Requirement<?> requirement = definition.getRequirement(parameterIndex);
            String name = requirement.getName();

            if (!results.has(name)) {
                return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved, missing " + name));
            }

            RequirementMatch<?, ?> requirementMatch = results.get(name);
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
