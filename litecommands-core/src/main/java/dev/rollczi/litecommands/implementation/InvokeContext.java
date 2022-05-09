package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.LiteInvocation;

import java.util.List;

class InvokeContext {

    private final LiteInvocation invocation;
    private final List<Object> parameters;

    InvokeContext(LiteInvocation invocation, List<Object> parameters) {
        this.invocation = invocation;
        this.parameters = parameters;
    }

    LiteInvocation getInvocation() {
        return invocation;
    }

    List<Object> getParameters() {
        return parameters;
    }

    public Object getParameter(int index) {
        return parameters.get(index);
    }

    static InvokeContext fromArgs(Object[] args) {
        return (InvokeContext) args[0];
    }

}
