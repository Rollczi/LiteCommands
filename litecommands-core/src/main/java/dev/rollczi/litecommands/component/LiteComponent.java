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
        private final List<LiteComponent> tracesOfResolvers = new ArrayList<>();

        private Data(Data data, LiteComponent resolver) {
            this.invocation = data.invocation;
            this.tracesOfResolvers.addAll(data.tracesOfResolvers);
            this.tracesOfResolvers.add(resolver);
        }

        private Data(LiteInvocation invocation) {
            this.invocation = invocation;
        }

        Data resolverNestingTracing(LiteComponent currentResolver) {
            if (this.invocation.arguments().length + 1 < tracesOfResolvers.size()) {
                throw new IllegalStateException();
            }

            return new Data(this, currentResolver);
        }

        public int getCurrentArgsCount(LiteComponent context) {
            return context.getScope().getName().isEmpty()
                    ? invocation.arguments().length + 1 - tracesOfResolvers.size()
                    : invocation.arguments().length - tracesOfResolvers.size();
        }

        public String getNextPredictedResolverName() {
            String[] arguments = this.invocation.arguments();
            int size = tracesOfResolvers.size();
            
            if (arguments.length == size) {
                return StringUtils.EMPTY;
            }

            if (arguments.length < size) {
                throw new IllegalStateException();
            }

            return arguments[size];
        }

        public List<LiteComponent> getTracesOfResolvers() {
            return tracesOfResolvers;
        }

        public LiteInvocation getInvocation() {
            return invocation;
        }

        public static Data create(LiteInvocation invocation) {
            return new Data(invocation);
        }

    }

}
