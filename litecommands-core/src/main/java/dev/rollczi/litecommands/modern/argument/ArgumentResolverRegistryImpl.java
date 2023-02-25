package dev.rollczi.litecommands.modern.argument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArgumentResolverRegistryImpl<SENDER> implements ArgumentResolverRegistry<SENDER> {

    private final DeterminantTypeIndex determinantTypeIndex = new DeterminantTypeIndex();
    private final UniversalIndex universalIndex = new UniversalIndex();

    @Override
    @SuppressWarnings("unchecked")
    public <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends Argument<DETERMINANT, EXPECTED>> resolver
    ) {
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> castedResolver = (ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>) resolver;

        if (indexKey.isUniversal()) {
            this.universalIndex.putResolver(indexKey, castedResolver);
            return;
        }

        this.determinantTypeIndex.putResolver(indexKey, castedResolver);
    }

    @Override
    public <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> getResolver(
        IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey
    ) {
        Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> resolverOptional = this.determinantTypeIndex.getResolver(indexKey);

        if (resolverOptional.isPresent()) {
            return resolverOptional;
        }

        return this.universalIndex.getResolver(indexKey);
    }

    class DeterminantTypeIndex {
        private final Map<Class<?>, ExpectedTypeIndex<?>> indexesByContextType = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver
        ) {
            ExpectedTypeIndex<DETERMINANT> expectedTypeIndex = (ExpectedTypeIndex<DETERMINANT>) this.indexesByContextType.computeIfAbsent(indexKey.determinantType(), key -> new ExpectedTypeIndex<>());

            expectedTypeIndex.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        public <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey
        ) {
            ExpectedTypeIndex<DETERMINANT> expectedTypeIndex = (ExpectedTypeIndex<DETERMINANT>) this.indexesByContextType.get(indexKey.determinantType());

            if (expectedTypeIndex == null) {
                return Optional.empty();
            }

            return expectedTypeIndex.getResolver(indexKey);
        }
    }


    class ExpectedTypeIndex<DETERMINANT> {
        private final Map<Class<?>, ContextTypeIndex<DETERMINANT, ?>> resolversByContext = new HashMap<>();

        @SuppressWarnings("unchecked")
        <EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver
        ) {
            ContextTypeIndex<DETERMINANT, EXPECTED> contextTypeIndex = (ContextTypeIndex<DETERMINANT, EXPECTED>) this.resolversByContext.computeIfAbsent(indexKey.expectedType(), key -> new ContextTypeIndex<>());

            contextTypeIndex.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey
        ) {
            ContextTypeIndex<DETERMINANT, EXPECTED> contextTypeIndex = (ContextTypeIndex<DETERMINANT, EXPECTED>) this.resolversByContext.get(indexKey.expectedType());

            if (contextTypeIndex == null) {
                return Optional.empty();
            }

            return contextTypeIndex.getResolver(indexKey);
        }
    }


    class ContextTypeIndex<DETERMINANT, EXPECTED> {
        private final Map<Class<?>, ArgumentKeyIndex<DETERMINANT, EXPECTED, ?>> containersByContext = new HashMap<>();

        @SuppressWarnings("unchecked")
        <ARGUMENT extends Argument<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver
        ) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT> resolverByArgumentKey = (ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT>) this.containersByContext.computeIfAbsent(indexKey.contextType(), key -> new ArgumentKeyIndex<>());

            resolverByArgumentKey.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <ARGUMENT extends Argument<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey
        ) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT> argumentKeyIndex = (ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT>) this.containersByContext.get(indexKey.contextType());

            if (argumentKeyIndex == null) {
                return Optional.empty();
            }

            return argumentKeyIndex.getResolver(indexKey);
        }
    }

    class ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> {
        private final Map<ArgumentKey, ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> resolversByKey = new HashMap<>();

        void putResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver
        ) {
            this.resolversByKey.put(indexKey.argumentKey(), resolver);
        }

        Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey
        ) {
            return Optional.ofNullable(this.resolversByKey.get(indexKey.argumentKey()));
        }
    }

    private class UniversalIndex {

        private final Map<Class<?>, ArgumentKeyIndex<Object, Object, Argument<Object, Object>>> map = new HashMap<>();

        @SuppressWarnings("unchecked")
        <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> void putResolver(IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey, ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT> index = (ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT>) this.map.computeIfAbsent(indexKey.expectedType(), key -> new ArgumentKeyIndex<>());

            index.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> getResolver(IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT> index = (ArgumentKeyIndex<DETERMINANT, EXPECTED, ARGUMENT>) this.map.get(indexKey.expectedType());

            if (index == null) {
                return Optional.empty();
            }

            return index.getResolver(indexKey);
        }


    }

}
