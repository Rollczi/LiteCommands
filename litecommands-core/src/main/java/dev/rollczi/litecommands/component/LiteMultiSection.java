package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.valid.ValidationInfo;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class LiteMultiSection extends AbstractComponent {

    private final String name;
    private final List<LiteSection> sections;

    LiteMultiSection(ScopeMetaData scopeMetaData, String name, List<LiteSection> sections) {
        super(scopeMetaData);
        this.name = name;
        this.sections = sections;
    }

    @Override
    public ExecutionResult resolveExecution(ContextOfResolving context) {
        ArrayList<LiteSection> filteredComponents = new ArrayList<>(sections);

        if (filteredComponents.isEmpty()) {
            return ExecutionResult.invalid(ValidationInfo.INVALID_USE, context, true);
        }

        //TODO [START]: LiteMultiSection and LiteSection has same logic
        ContextOfResolving candidateContext = context.resolverNestingTracing(filteredComponents.get(0));
        filteredComponents.removeIf(component -> !component.hasPermission(context));

        if (filteredComponents.isEmpty()) {
            return ExecutionResult.invalid(ValidationInfo.NO_PERMISSION, candidateContext);
        }

        Option<ExecutionResult> lastInvalid = Option.none();

        for (LiteComponent component : filteredComponents) {
            if (!component.hasValidArgs(context)) {
                continue;
            }

            ExecutionResult result = component.resolveExecution(context);

            if (result.isValid() || !result.canIgnore()) {
                return result;
            }

            lastInvalid = Option.of(result);
        }

        return lastInvalid.orElseGet(ExecutionResult.invalid(ValidationInfo.INVALID_USE, candidateContext));
        //TODO [END]
    }

    @Override
    public boolean hasPermission(ContextOfResolving context) {
        for (LiteSection resolver : sections) {
            if (resolver.hasPermission(context)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> resolveCompletion(ContextOfResolving context) {
        List<String> list = new ArrayList<>();

        for (LiteSection resolver : sections) {
            list.addAll(resolver.resolveCompletion(context));
        }

        return list;
    }

    public Collection<LiteSection> getSections() {
        return Collections.unmodifiableCollection(sections);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ScopeMetaData scopeMetaData;
        private String name;
        private final List<LiteSection> sections = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder scopeInformation(ScopeMetaData scopeMetaData) {
            this.scopeMetaData = scopeMetaData;
            return this;
        }

        public <T extends LiteSection> Builder sections(Collection<T> resolvers) {
            this.sections.addAll(PandaStream.of(resolvers).toList());
            return this;
        }

        @SafeVarargs
        public final <T extends LiteSection> Builder sections(T... resolvers) {
            this.sections.addAll(PandaStream.of(resolvers).toList());
            return this;
        }

        public LiteMultiSection build() {
            for (LiteSection resolver : sections) {
                if (!resolver.getScope().getName().equalsIgnoreCase(name)) {
                    throw new IllegalStateException();
                }
            }

            return new LiteMultiSection(scopeMetaData, name, sections);
        }

    }

}
