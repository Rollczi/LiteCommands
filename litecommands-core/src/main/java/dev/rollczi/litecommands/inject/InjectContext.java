package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;

import java.util.ArrayList;
import java.util.List;

public class InjectContext {

    private final LiteInvocation invocation;
    private final List<LiteComponent> traces = new ArrayList<>();

    public InjectContext(LiteComponent.ContextOfResolving data) {
        this.invocation = data.getInvocation();
        this.traces.addAll(data.getTracesOfResolvers());
    }

    public List<LiteComponent> getTraces() {
        return traces;
    }

    public LiteInvocation getInvocation() {
        return invocation;
    }

    public int getArgsMargin() {
        return traces.get(traces.size() - 1).getScope().getName().isEmpty()
                ? traces.size() - 2
                : traces.size() - 1;
    }

}
