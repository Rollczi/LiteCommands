package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;

import java.util.ArrayList;
import java.util.List;

public class InjectContext {

    private final LiteInvocation invocation;
    private final List<LiteComponent> traces = new ArrayList<>();

    public InjectContext(LiteComponent.MetaData data, LiteComponent current) {
        this.invocation = data.getInvocation();
        this.traces.addAll(data.getTracesOfResolvers());
        this.traces.add(current);
    }

    public List<LiteComponent> getTraces() {
        return traces;
    }

    public LiteInvocation getInvocation() {
        return invocation;
    }

    public int getArgsCount() {
        return traces.get(traces.size() - 1).getScope().getName().isEmpty()
                ? invocation.arguments().length + 1 - traces.size()
                : invocation.arguments().length - traces.size();
    }

}
