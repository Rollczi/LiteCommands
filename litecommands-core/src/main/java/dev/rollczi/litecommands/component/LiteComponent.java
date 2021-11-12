package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.LiteInvocation;
import panda.utilities.StringUtils;

import java.util.ArrayList;
import java.util.List;

public interface LiteComponent {

    void resolve(Data data);

    ScopeMetaData getScope();

    class Data {
        private final LiteInvocation invocation;
        private final List<LiteComponent> traces = new ArrayList<>();

        private Data(Data data, LiteComponent resolver) {
            this.invocation = data.invocation;
            this.traces.addAll(data.traces);
            this.traces.add(resolver);
        }

        private Data(LiteInvocation invocation) {
            this.invocation = invocation;
        }

        Data traceNesting(LiteComponent currentResolver) {
            if (this.invocation.arguments().length + 1 < traces.size()) {
                throw new IllegalStateException();
            }

            return new Data(this, currentResolver);
        }

        public int getCurrentArgsCount(LiteComponent context) {
            return context.getScope().getName().isEmpty()
                    ? invocation.arguments().length + 1 - traces.size()
                    : invocation.arguments().length - traces.size();
        }

        public String getNextCommandTrace() {
            if (this.invocation.arguments().length == traces.size()) {
                return StringUtils.EMPTY;
            }

            return this.invocation.arguments()[traces.size()];
        }

        public List<LiteComponent> getTracedResolvers() {
            return traces;
        }

        public LiteInvocation getInvocation() {
            return invocation;
        }

        public static Data create(LiteInvocation invocation) {
            return new Data(invocation);
        }

    }

}
