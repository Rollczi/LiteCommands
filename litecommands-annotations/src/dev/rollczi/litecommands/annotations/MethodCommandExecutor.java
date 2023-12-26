package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorContext;
import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorService;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.AbstractCommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.invalidusage.InvalidUsageException;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementMatch;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import dev.rollczi.litecommands.validator.ValidatorResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER> {

    private final Method method;
    private final Object instance;
    private final MethodDefinition definition;
    private final MethodValidatorService<SENDER> validatorService;

    MethodCommandExecutor(
        CommandRoute<SENDER> parent,
        Method method,
        Object instance,
        MethodDefinition definition,
        Meta meta,
        MethodValidatorService<SENDER> validatorService
    ) {
        super(parent, definition.getArguments(), definition.getContextRequirements(), definition.getBindRequirements());
        this.method = method;
        this.instance = instance;
        this.definition = definition;
        this.validatorService = validatorService;
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
            Parameter parameter = method.getParameters()[parameterIndex];
            Class<?> type = parameter.getType();

            if (unwrapped == null && type.isPrimitive()) {
                Class<?> primitive = ReflectUtil.primitiveToBoxed(type);

                return CommandExecutorMatchResult.failed(new LiteCommandsReflectInvocationException(
                    method,
                    parameter,
                    "Null value cannot be assigned to primitive type. Replace " + type.getSimpleName() + " with " + primitive.getSimpleName()
                ));
            }

            objects[parameterIndex] = unwrapped;
        }

        ValidatorResult result = validatorService.validate(new MethodValidatorContext<>(results, definition, instance, method, objects));

        if (result.isInvalid()) {
            return CommandExecutorMatchResult.failed(result.getInvalidResult());
        }

        return CommandExecutorMatchResult.success(() -> {
            try {
                this.method.setAccessible(true);

                return CommandExecuteResult.success(this, this.method.invoke(this.instance, objects));
            }
            catch (IllegalAccessException exception) {
                throw new LiteCommandsReflectInvocationException(this.method, "Cannot access method", exception);
            }
            catch (InvocationTargetException exception) {
                Throwable targetException = exception.getTargetException();

                if (targetException instanceof InvalidUsageException) { //TODO: Use invalid usage handler (when InvalidUsage.Cause is mapped to InvalidUsage)
                    return CommandExecuteResult.failed(this, ((InvalidUsageException) targetException).getErrorResult());
                }

                throw new LiteCommandsReflectInvocationException(this.method, "Command method threw " + targetException.getClass().getSimpleName(), targetException);
            }
        });
    }

}
