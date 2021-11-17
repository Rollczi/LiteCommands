package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.LiteInvocation;
import panda.utilities.StringUtils;

import java.util.ArrayList;
import java.util.List;

public interface LiteComponent {

    void resolveExecution(MetaData data);

    List<String> resolveCompletion(MetaData data);

    ScopeMetaData getScope();

    class MetaData {
        protected final LiteInvocation invocation;
        protected final List<LiteComponent> tracesOfResolvers = new ArrayList<>();

        private MetaData(MetaData data, LiteComponent resolver) {
            this.invocation = data.invocation;
            this.tracesOfResolvers.addAll(data.tracesOfResolvers);
            this.tracesOfResolvers.add(resolver);
        }

        private MetaData(LiteInvocation invocation) {
            this.invocation = invocation;
        }

        MetaData resolverNestingTracing(LiteComponent currentResolver) {
            if (this.invocation.arguments().length + 1 < tracesOfResolvers.size()) {
                throw new IllegalStateException();
            }

            return new MetaData(this, currentResolver);
        }

        public int getCurrentArgsCount(LiteComponent context) {
            return context.getScope().getName().isEmpty()
                    ? invocation.arguments().length + 1 - tracesOfResolvers.size()
                    : invocation.arguments().length - tracesOfResolvers.size();
        }

        public boolean isLastResolver() {
            return invocation.arguments().length - 1 <= tracesOfResolvers.size();
        }

        public String getNextPredictedPartOfSuggestion() {
            if (this.isLastResolver()) {
                return StringUtils.EMPTY;
            }

            return this.invocation.arguments()[tracesOfResolvers.size()];
        }

        public String getCurrentPartOfCommand() {
            return this.invocation.arguments()[tracesOfResolvers.size()];
        }

        public List<LiteComponent> getTracesOfResolvers() {
            return tracesOfResolvers;
        }

        public LiteInvocation getInvocation() {
            return invocation;
        }

        public static MetaData create(LiteInvocation invocation) {
            return new MetaData(invocation);
        }

    }

}
