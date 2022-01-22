package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.valid.ValidationInfo;
import panda.std.Option;
import panda.std.stream.PandaStream;
import panda.utilities.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class LiteSection extends AbstractComponent {

    private final Map<String, LiteComponent> resolvers;

    LiteSection(ScopeMetaData scopeMetaData, Map<String, LiteComponent> resolvers) {
        super(scopeMetaData);
        this.resolvers = resolvers;
    }

    @Override
    public ExecutionResult resolveExecution(ContextOfResolving context) {
        String command = context.getCurrentPartOfCommand();
        ContextOfResolving currentContext = context.resolverNestingTracing(this);

        List<LiteComponent> filteredComponents = PandaStream.of(this.resolvers.values())
                .filter(component -> component.getScope().getName().equalsIgnoreCase(command))
                .toList();

        if (filteredComponents.isEmpty()) {
            Option<LiteComponent> emptyComponentMatch = Option.of(this.resolvers.get(StringUtils.EMPTY));

            if (emptyComponentMatch.isEmpty()) {
                return ExecutionResult.invalid(ValidationInfo.INVALID_USE, currentContext, true);
            }

            filteredComponents.add(emptyComponentMatch.get());
        }

        LiteComponent candidate = filteredComponents.get(0);
        ContextOfResolving candidateContext = currentContext.resolverNestingTracing(candidate);
        filteredComponents.removeIf(component -> !component.getMissingPermission(currentContext).isEmpty());

        if (filteredComponents.isEmpty()) {
            List<String> missingPermission = candidate.getMissingPermission(currentContext);

            return ExecutionResult.invalidPermission(missingPermission, currentContext);
        }

        Option<ExecutionResult> lastInvalid = Option.none();

        for (LiteComponent component : filteredComponents) {
            if (!component.hasValidArgs(currentContext)) {
                lastInvalid = Option.of(ExecutionResult.invalid(ValidationInfo.INVALID_USE, currentContext.resolverNestingTracing(component)));
                continue;
            }

            ExecutionResult result = component.resolveExecution(currentContext);

            if (result.isValid() || !result.canIgnore()) {
                return result;
            }

            lastInvalid = Option.of(result);
        }

        return lastInvalid.orElseGet(ExecutionResult.invalid(ValidationInfo.INVALID_USE, candidateContext));
    }

    @Override
    public List<String> resolveCompletion(ContextOfResolving data) {
        ContextOfResolving currentContextOfResolving = data.resolverNestingTracing(this);

        // /command subcommand|
        if (data.isLastResolver()) {
            ArrayList<String> suggestions = new ArrayList<>(resolvers.keySet());

            if (suggestions.remove(StringUtils.EMPTY)) {
                suggestions.addAll(resolvers.get(StringUtils.EMPTY).resolveCompletion(currentContextOfResolving));
            }

            return suggestions;
        }

        String partCommand = data.getNextPredictedPartOfSuggestion();

        // /command subcommand |litecomponent
        if (partCommand.isEmpty()) {
            List<String> suggestions = new ArrayList<>(resolvers.keySet()); // suggestions (subcommands)
            LiteComponent executor = resolvers.get(StringUtils.EMPTY);

            if (executor != null) {
                suggestions.remove(StringUtils.EMPTY);
                suggestions.addAll(executor.resolveCompletion(currentContextOfResolving)); // suggestions (arguments)
            }

            return suggestions;
        }

        // /command subcommand| litecomponent
        for (Map.Entry<String, LiteComponent> entry : resolvers.entrySet()) {
            if (!partCommand.equalsIgnoreCase(entry.getKey())) {
                continue;
            }

            return entry.getValue().resolveCompletion(currentContextOfResolving);
        }

        String[] arguments = data.invocation.arguments();
        LiteComponent component = resolvers.get(StringUtils.EMPTY);

        // /command subcommand argument |[...]
        if (component instanceof LiteExecution && arguments.length != 0) {
            LiteExecution liteExecution = (LiteExecution) component;
            List<String> oldSuggestions = liteExecution.generateCompletion(currentContextOfResolving.getCurrentArgsCount(this) - 1, currentContextOfResolving);

            // /command subcommand argument |argument
            if (oldSuggestions.contains(arguments[arguments.length - 2])) {
                return component.resolveCompletion(currentContextOfResolving);
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

        public <T extends LiteComponent> Builder resolvers(Collection<T> resolvers) {
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
