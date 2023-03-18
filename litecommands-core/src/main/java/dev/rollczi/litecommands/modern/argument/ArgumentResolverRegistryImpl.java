package dev.rollczi.litecommands.modern.argument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArgumentResolverRegistryImpl<SENDER> implements ArgumentResolverRegistry<SENDER> {

    private final ExpectedTypeIndex determinantTypeIndex = new ExpectedTypeIndex();
    private final UniversalIndex universalIndex = new UniversalIndex();

    @Override
    @SuppressWarnings("unchecked")
    public <EXPECTED, ARGUMENT extends Argument<EXPECTED>> void registerResolver(
        IndexKey<EXPECTED, ARGUMENT> indexKey,
        ArgumentParser<SENDER, EXPECTED, ? extends Argument<EXPECTED>> resolver
    ) {
        ArgumentParser<SENDER, EXPECTED, ARGUMENT> castedResolver = (ArgumentParser<SENDER, EXPECTED, ARGUMENT>) resolver;

        if (indexKey.isUniversal()) {
            this.universalIndex.putResolver(indexKey, castedResolver);
            return;
        }

        this.determinantTypeIndex.putResolver(indexKey, castedResolver);
    }

    @Override
    public <EXPECTED> Optional<ArgumentParser<SENDER, EXPECTED, Argument<EXPECTED>>> getResolver(IndexKey<EXPECTED, Argument<EXPECTED>> indexKey) {
        Optional<ArgumentParser<SENDER, EXPECTED, Argument<EXPECTED>>> resolver = this.determinantTypeIndex.getResolver(indexKey);

        if (resolver.isPresent()) {
            return resolver;
        }

        return this.universalIndex.getResolver(indexKey);
    }

    class ExpectedTypeIndex {
        private final Map<Class<?>, ContextTypeIndex<?>> resolversByContext = new HashMap<>();

        @SuppressWarnings("unchecked")
        <EXPECTED, ARGUMENT extends Argument<EXPECTED>> void putResolver(
            IndexKey<EXPECTED, ARGUMENT> indexKey,
            ArgumentParser<SENDER, EXPECTED, ARGUMENT> resolver
        ) {
            ContextTypeIndex<EXPECTED> contextTypeIndex = (ContextTypeIndex<EXPECTED>) this.resolversByContext.computeIfAbsent(indexKey.expectedType(), key -> new ContextTypeIndex<>());

            contextTypeIndex.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <EXPECTED, ARGUMENT extends Argument<EXPECTED>> Optional<ArgumentParser<SENDER, EXPECTED, ARGUMENT>> getResolver(
            IndexKey<EXPECTED, ARGUMENT> indexKey
        ) {
            ContextTypeIndex<EXPECTED> contextTypeIndex = (ContextTypeIndex<EXPECTED>) this.resolversByContext.get(indexKey.expectedType());

            if (contextTypeIndex == null) {
                return Optional.empty();
            }

            return contextTypeIndex.getResolver(indexKey);
        }
    }


    class ContextTypeIndex<EXPECTED> {
        private final Map<Class<?>, ArgumentKeyIndex<EXPECTED, ?>> containersByContext = new HashMap<>();

        @SuppressWarnings("unchecked")
        <ARGUMENT extends Argument<EXPECTED>> void putResolver(
            IndexKey<EXPECTED, ARGUMENT> indexKey,
            ArgumentParser<SENDER, EXPECTED, ARGUMENT> resolver
        ) {
            ArgumentKeyIndex<EXPECTED, ARGUMENT> resolverByArgumentKey = (ArgumentKeyIndex<EXPECTED, ARGUMENT>) this.containersByContext.computeIfAbsent(indexKey.argumentType(), key -> new ArgumentKeyIndex<>());

            resolverByArgumentKey.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <ARGUMENT extends Argument<EXPECTED>> Optional<ArgumentParser<SENDER, EXPECTED, ARGUMENT>> getResolver(
            IndexKey<EXPECTED, ARGUMENT> indexKey
        ) {
            ArgumentKeyIndex<EXPECTED, ARGUMENT> argumentKeyIndex = (ArgumentKeyIndex<EXPECTED, ARGUMENT>) this.containersByContext.get(indexKey.argumentType());

            if (argumentKeyIndex == null) {
                return Optional.empty();
            }

            return argumentKeyIndex.getResolver(indexKey);
        }
    }

    class ArgumentKeyIndex<EXPECTED, ARGUMENT extends Argument<EXPECTED>> {
        private final Map<ArgumentKey, ArgumentParser<SENDER, EXPECTED, ARGUMENT>> resolversByKey = new HashMap<>();

        void putResolver(
            IndexKey<EXPECTED, ARGUMENT> indexKey,
            ArgumentParser<SENDER, EXPECTED, ARGUMENT> resolver
        ) {
            this.resolversByKey.put(indexKey.argumentKey(), resolver);
        }

        Optional<ArgumentParser<SENDER, EXPECTED, ARGUMENT>> getResolver(
            IndexKey<EXPECTED, ARGUMENT> indexKey
        ) {
            return Optional.ofNullable(this.resolversByKey.get(indexKey.argumentKey()));
        }
    }

    private class UniversalIndex {

        private final Map<Class<?>, ArgumentKeyIndex<Object, Argument<Object>>> map = new HashMap<>();

        @SuppressWarnings("unchecked")
        <EXPECTED, ARGUMENT extends Argument<EXPECTED>> void putResolver(IndexKey<EXPECTED, ARGUMENT> indexKey, ArgumentParser<SENDER, EXPECTED, ARGUMENT> resolver) {
            ArgumentKeyIndex<EXPECTED, ARGUMENT> index = (ArgumentKeyIndex<EXPECTED, ARGUMENT>) this.map.computeIfAbsent(indexKey.expectedType(), key -> new ArgumentKeyIndex<>());

            index.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <EXPECTED, ARGUMENT extends Argument<EXPECTED>> Optional<ArgumentParser<SENDER, EXPECTED, ARGUMENT>> getResolver(IndexKey<EXPECTED, ARGUMENT> indexKey) {
            ArgumentKeyIndex<EXPECTED, ARGUMENT> index = (ArgumentKeyIndex<EXPECTED, ARGUMENT>) this.map.get(indexKey.expectedType());

            if (index == null) {
                return Optional.empty();
            }

            return index.getResolver(indexKey);
        }


    }

}
