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
import java.util.function.Supplier;

public class MethodCommandExecutor<SENDER> extends AbstractCommandExecutor<SENDER> {

    private final Method method;
    private final Parameter[] parameters;
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
        this.method.setAccessible(true);
        this.parameters = method.getParameters();
        this.instance = instance;
        this.definition = definition;
        this.validatorService = validatorService;
        this.meta.apply(meta);
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public MethodDefinition getDefinition() {
        return definition;
    }

    @Override
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> results) {
        Object[] objects = new Object[method.getParameterCount()];

        int parameterIndex = 0;
        for (Requirement<?> requirement : definition.getRequirements()) {
            String name = requirement.getName();

            RequirementMatch requirementMatch = results.get(name);

            if (requirementMatch == null) {
                return CommandExecutorMatchResult.failed(new IllegalStateException("Not all parameters are resolved, missing " + name));
            }

            Object unwrapped = requirementMatch.getResult().unwrap();
            Parameter parameter = parameters[parameterIndex];
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
            parameterIndex++;
        }

        ValidatorResult result = validatorService.validate(new MethodValidatorContext<>(results, definition, instance, method, objects));

        if (result.isInvalid()) {
            return CommandExecutorMatchResult.failed(result.getInvalidResult());
        }

        return CommandExecutorMatchResult.success(new FinalCommandExecutor(objects));
    }

    private class FinalCommandExecutor implements Supplier<CommandExecuteResult> {
        private final Object[] objects;

        public FinalCommandExecutor(Object[] objects) {
            this.objects = objects;
        }

        @Override
        public CommandExecuteResult get() {
            try {
                return CommandExecuteResult.success(MethodCommandExecutor.this, MethodCommandExecutor.this.method.invoke(MethodCommandExecutor.this.instance, objects));
            } catch (IllegalAccessException exception) {
                throw new LiteCommandsReflectInvocationException(MethodCommandExecutor.this.method, "Cannot access method", exception);
            } catch (InvocationTargetException exception) {
                Throwable targetException = exception.getTargetException();

                if (targetException instanceof InvalidUsageException) { //TODO: Use invalid usage handler (when InvalidUsage.Cause is mapped to InvalidUsage)
                    return CommandExecuteResult.failed(MethodCommandExecutor.this, ((InvalidUsageException) targetException).getErrorResult());
                }

                throw new LiteCommandsReflectInvocationException(MethodCommandExecutor.this.method, "Command method threw " + targetException.getClass().getSimpleName(), targetException);
            }
        }
    }
}
