package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArgumentResolverRegistryImpl<SENDER> implements ArgumentResolverRegistry<SENDER> {

    private final DeterminantTypeIndex determinantTypeIndex = new DeterminantTypeIndex();
    private final UniversalIndex universalIndex = new UniversalIndex();

    @Override
    @SuppressWarnings("unchecked")
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends ArgumentContext<DETERMINANT, EXPECTED>> resolver
    ) {
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> castedResolver = (ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>) resolver;

        if (indexKey.isUniversal()) {
            universalIndex.putResolver(indexKey, castedResolver);
            return;
        }

        determinantTypeIndex.putResolver(indexKey, castedResolver);
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
    ) {
        Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> resolverOptional = determinantTypeIndex.getResolver(indexKey);

        if (resolverOptional.isPresent()) {
            return resolverOptional;
        }

        return universalIndex.getResolver(indexKey);
    }

    class DeterminantTypeIndex {
        private final Map<Class<?>, ExpectedTypeIndex<?>> indexesByContextType = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver
        ) {
            ExpectedTypeIndex<DETERMINANT> expectedTypeIndex = (ExpectedTypeIndex<DETERMINANT>) this.indexesByContextType.computeIfAbsent(indexKey.determinantType(), key -> new ExpectedTypeIndex<>());

            expectedTypeIndex.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
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
        <EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver
        ) {
            ContextTypeIndex<DETERMINANT, EXPECTED> contextTypeIndex = (ContextTypeIndex<DETERMINANT, EXPECTED>) this.resolversByContext.computeIfAbsent(indexKey.expectedType(), key -> new ContextTypeIndex<>());

            contextTypeIndex.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
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
        <CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver
        ) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT> resolverByArgumentKey = (ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT>) this.containersByContext.computeIfAbsent(indexKey.contextType(), key -> new ArgumentKeyIndex<>());

            resolverByArgumentKey.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
        ) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT> argumentKeyIndex = (ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT>) this.containersByContext.get(indexKey.contextType());

            if (argumentKeyIndex == null) {
                return Optional.empty();
            }

            return argumentKeyIndex.getResolver(indexKey);
        }
    }

    class ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> {
        private final Map<ArgumentKey, ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> resolversByKey = new HashMap<>();

        void putResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver
        ) {
            this.resolversByKey.put(indexKey.argumentKey(), resolver);
        }

        Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
        ) {
            return Optional.ofNullable(this.resolversByKey.get(indexKey.argumentKey()));
        }
    }

    private class UniversalIndex {

        private final Map<Class<?>, ArgumentKeyIndex<Object, Object, ArgumentContext<Object, Object>>> map = new HashMap<>();

        @SuppressWarnings("unchecked")
        <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void putResolver(IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey, ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT> index = (ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT>) this.map.computeIfAbsent(indexKey.expectedType(), key -> new ArgumentKeyIndex<>());

            index.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT> index = (ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT>) this.map.get(indexKey.expectedType());

            if (index == null) {
                return Optional.empty();
            }

            return index.getResolver(indexKey);
        }


    }

}
