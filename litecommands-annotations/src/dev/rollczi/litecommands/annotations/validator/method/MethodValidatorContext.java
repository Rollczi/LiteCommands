package dev.rollczi.litecommands.annotations.validator.method;

import dev.rollczi.litecommands.invocation.Invocation;

import java.lang.reflect.Method;

public class MethodValidatorContext<SENDER> {

    private final Invocation<SENDER> invocation;
    private final Object command;
    private final Method method;
    private final Object[] args;

    public MethodValidatorContext(Invocation<SENDER> invocation, Object command, Method method, Object[] args) {
        this.invocation = invocation;
        this.command = command;
        this.method = method;
        this.args = args;
    }

    public Invocation<SENDER> getInvocation() {
        return invocation;
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
