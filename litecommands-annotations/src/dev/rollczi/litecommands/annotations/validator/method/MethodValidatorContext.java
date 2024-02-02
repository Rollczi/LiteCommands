package dev.rollczi.litecommands.annotations.validator.method;

import dev.rollczi.litecommands.annotations.MethodDefinition;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.requirement.RequirementsResult;

import java.lang.reflect.Method;

public class MethodValidatorContext<SENDER> {

    private final RequirementsResult<SENDER> requirementsResult;
    private final MethodDefinition definition;
    private final Object command;
    private final Method method;
    private final Object[] args;

    public MethodValidatorContext(RequirementsResult<SENDER> requirementsResult, MethodDefinition definition, Object command, Method method, Object[] args) {
        this.requirementsResult = requirementsResult;
        this.definition = definition;
        this.command = command;
        this.method = method;
        this.args = args;
    }

    public Invocation<SENDER> getInvocation() {
        return requirementsResult.getInvocation();
    }

    public RequirementsResult<SENDER> getRequirementsResult() {
        return requirementsResult;
    }

    public MethodDefinition getDefinition() {
        return definition;
    }

    public Method getMethod() {
        return method;
    }

    public Object getCommand() {
        return command;
    }

    public Object[] getArgs() {
        return args;
    }

}
