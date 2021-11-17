package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.valid.ValidationInfo;
import panda.std.stream.PandaStream;
import panda.utilities.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    public void resolveExecution(MetaData data) {
        LiteComponent resolver = resolvers.getOrDefault(data.getNextPredictedPartOfSuggestion(), resolvers.get(StringUtils.EMPTY));

        whenWithContext(resolver == null, ValidationInfo.COMMAND_NO_FOUND, data, this);

        resolver.resolveExecution(data.resolverNestingTracing(this));
    }

    @Override
    public List<String> resolveCompletion(MetaData data) {
        MetaData nestedMetaData = data.resolverNestingTracing(this);

        if (data.isLastResolver()) {
            ArrayList<String> suggestions = new ArrayList<>(resolvers.keySet());

            if (suggestions.remove(StringUtils.EMPTY)) {
                suggestions.addAll(resolvers.get(StringUtils.EMPTY).resolveCompletion(nestedMetaData));
            }

            return suggestions;
        }

        String partCommand = data.getCurrentPartOfCommand();

        if (partCommand.isEmpty()) {
            List<String> suggestions = new ArrayList<>(resolvers.keySet());
            LiteComponent executor = resolvers.get(StringUtils.EMPTY);
            suggestions.remove(StringUtils.EMPTY);

            if (executor != null) {
                suggestions.addAll(executor.resolveCompletion(nestedMetaData));
            }

            return suggestions;
        }

        for (Map.Entry<String, LiteComponent> entry : resolvers.entrySet()) {
            if (!partCommand.equalsIgnoreCase(entry.getKey())) {
                continue;
            }

            return entry.getValue().resolveCompletion(nestedMetaData);
        }

        String[] arguments = data.invocation.arguments();
        LiteComponent component = resolvers.get(StringUtils.EMPTY);

        if (component != null && arguments.length != 0 && component instanceof LiteExecution liteExecution) {
            List<String> oldSuggestions = liteExecution.getExecutorCompletion(data, nestedMetaData.getCurrentArgsCount(this) - 1);

            if (oldSuggestions.contains(arguments[arguments.length - 2])) {
                return component.resolveCompletion(nestedMetaData);
            }
        }

        return Collections.emptyList();
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
            return new LiteSection(scopeMetaData, resolvers);
        }

    }

}
