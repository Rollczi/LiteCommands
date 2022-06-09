package dev.rollczi.litecommands.injector;

import dev.rollczi.litecommands.command.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvokeContext<CONTEXT> {

    private final Invocation<CONTEXT> invocation;
    private final List<Object> injectable = new ArrayList<>();

    public InvokeContext(Invocation<CONTEXT> invocation, List<Object> injectable) {
        this.invocation = invocation;
        this.injectable.addAll(injectable);
    }

    public Invocation<CONTEXT> getInvocation() {
        return invocation;
    }

    public List<Object> getInjectable() {
        return Collections.unmodifiableList(injectable);
    }

}
