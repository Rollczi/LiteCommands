package dev.rollczi.litecommands.modern.argument.invocation;

import dev.rollczi.litecommands.modern.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.argument.ArgumentKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArgumentResolverRegistryImpl<SENDER> implements ArgumentResolverRegistry<SENDER> {

    private final ContextTypeIndex contextTypeIndex = new ContextTypeIndex();

    @Override
    @SuppressWarnings("unchecked")
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends ArgumentContext<DETERMINANT, EXPECTED>> resolver
    ) {
        contextTypeIndex.putResolver(indexKey, (ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>) resolver);
        
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
    ) {
        return contextTypeIndex.getResolver(indexKey);
    }

    class ContextTypeIndex {
        private final Map<Class<?>, ReturnTypeIndex<?>> indexesByContextType = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver
        ) {
            ReturnTypeIndex<DETERMINANT> expectedTypeIndex = (ReturnTypeIndex<DETERMINANT>) this.indexesByContextType.computeIfAbsent(indexKey.contextType(), key -> new ReturnTypeIndex<>());

            expectedTypeIndex.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
        ) {
            ReturnTypeIndex<DETERMINANT> expectedTypeIndex = (ReturnTypeIndex<DETERMINANT>) this.indexesByContextType.get(indexKey.contextType());

            if (expectedTypeIndex == null) {
                return Optional.empty();
            }

            return expectedTypeIndex.getResolver(indexKey);
        }
    }


    class ReturnTypeIndex<DETERMINANT> {
        private final Map<Class<?>, ContextBoxTypeIndex<DETERMINANT, ?>> resolversByContext = new HashMap<>();

        @SuppressWarnings("unchecked")
        <EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver
        ) {
            ContextBoxTypeIndex<DETERMINANT, EXPECTED> contextBoxTypeIndex = (ContextBoxTypeIndex<DETERMINANT, EXPECTED>) this.resolversByContext.computeIfAbsent(indexKey.expectedType(), key -> new ContextBoxTypeIndex<>());

            contextBoxTypeIndex.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
        ) {
            ContextBoxTypeIndex<DETERMINANT, EXPECTED> contextBoxTypeIndex = (ContextBoxTypeIndex<DETERMINANT, EXPECTED>) this.resolversByContext.get(indexKey.expectedType());

            if (contextBoxTypeIndex == null) {
                return Optional.empty();
            }

            return contextBoxTypeIndex.getResolver(indexKey);
        }
    }


    class ContextBoxTypeIndex<DETERMINANT, EXPECTED> {
        private final Map<Class<?>, ArgumentKeyIndex<DETERMINANT, EXPECTED, ?>> containersByContextBox = new HashMap<>();

        @SuppressWarnings("unchecked")
        <CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void putResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
            ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver
        ) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT> resolverByArgumentKey = (ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT>) this.containersByContextBox.computeIfAbsent(indexKey.boxType(), key -> new ArgumentKeyIndex<>());

            resolverByArgumentKey.putResolver(indexKey, resolver);
        }

        @SuppressWarnings("unchecked")
        <CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
            IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
        ) {
            ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT> argumentKeyIndex = (ArgumentKeyIndex<DETERMINANT, EXPECTED, CONTEXT>) this.containersByContextBox.get(indexKey.boxType());

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

}
