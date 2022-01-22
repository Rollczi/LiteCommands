package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import panda.utilities.StringUtils;

import java.util.ArrayList;
import java.util.List;

public interface LiteComponent {

    ExecutionResult resolveExecution(ContextOfResolving data);

    List<String> resolveCompletion(ContextOfResolving data);

    ScopeMetaData getScope();

    List<String> getMissingPermission(ContextOfResolving context);

    boolean hasValidArgs(ContextOfResolving context);

    class ContextOfResolving {
        protected final LiteInvocation invocation;
        protected final List<LiteComponent> tracesOfResolvers = new ArrayList<>();

        private ContextOfResolving(ContextOfResolving data, LiteComponent resolver) {
            this.invocation = data.invocation;
            this.tracesOfResolvers.addAll(data.tracesOfResolvers);
            this.tracesOfResolvers.add(resolver);
        }

        private ContextOfResolving(LiteInvocation invocation) {
            this.invocation = invocation;
        }

        public ContextOfResolving resolverNestingTracing(LiteComponent currentResolver) {
            if (this.invocation.arguments().length + 1 < tracesOfResolvers.size()) {
                throw new IllegalStateException();
            }

            return new ContextOfResolving(this, currentResolver);
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
            return this.invocation.arguments()[tracesOfResolvers.size()];
        }

        public String getCurrentPartOfCommand() {
            if (this.invocation.arguments().length < tracesOfResolvers.size() + 1) {
                return StringUtils.EMPTY;
            }

            return this.invocation.arguments()[tracesOfResolvers.size()];
        }

        public List<LiteComponent> getTracesOfResolvers() {
            return tracesOfResolvers;
        }

        public int getArgsMargin() {
            return tracesOfResolvers.get(tracesOfResolvers.size() - 1).getScope().getName().isEmpty()
                    ? tracesOfResolvers.size() - 2
                    : tracesOfResolvers.size() - 1;
        }

        public LiteInvocation getInvocation() {
            return invocation;
        }

        public static ContextOfResolving create(LiteInvocation invocation) {
            return new ContextOfResolving(invocation);
        }

    }

}
