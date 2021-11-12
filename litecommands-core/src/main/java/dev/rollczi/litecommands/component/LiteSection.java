package dev.rollczi.litecommands.component;

import com.google.common.collect.ImmutableMap;
import dev.rollczi.litecommands.valid.ValidationInfo;
import panda.std.stream.PandaStream;
import panda.utilities.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static dev.rollczi.litecommands.valid.Valid.whenWithContext;

public final class LiteSection extends AbstractComponent {

    private final Map<String, LiteComponent> resolvers;

    LiteSection(ScopeMetaData scopeMetaData, Map<String, LiteComponent> resolvers) {
        super(scopeMetaData);
        this.resolvers = resolvers;
    }

    @Override
    public void resolve(Data data) {
        LiteComponent resolver = resolvers.getOrDefault(data.getNextCommandTrace(), resolvers.get(StringUtils.EMPTY));

        whenWithContext(resolver == null, ValidationInfo.COMMAND_NO_FOUND, data, this);

        resolver.resolve(data.traceNesting(this));
    }

    public Collection<LiteComponent> getResolvers() {
        return Collections.unmodifiableCollection(resolvers.values());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ScopeMetaData scopeMetaData;
        private final HashMap<String, LiteComponent> resolvers = new HashMap<>();

        public Builder scopeInformation(ScopeMetaData scopeMetaData) {
            this.scopeMetaData = scopeMetaData;
            return this;
        }

        public <T extends LiteComponent> Builder resolvers(Set<T> resolvers) {
            this.resolvers.putAll(PandaStream.of(resolvers)
                    .toMap(component -> component.getScope().getName(), Function.identity()));
            return this;
        }

        @SafeVarargs
        public final <T extends LiteComponent> Builder resolvers(T... resolvers) {
            this.resolvers.putAll(PandaStream.of(resolvers)
                    .toMap(component -> component.getScope().getName(), Function.identity()));
            return this;
        }

        public LiteSection build() {
            return new LiteSection(scopeMetaData, ImmutableMap.copyOf(resolvers));
        }

    }

}
