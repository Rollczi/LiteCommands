package dev.rollczi.litecommands.modern.argument.invocation;

import dev.rollczi.litecommands.modern.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResolverRegistry.IndexKey;

public class ArgumentService<SENDER> {

    private final ArgumentResolverRegistry<SENDER> resolverRegistry = new ArgumentResolverRegistryImpl<>();

    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> ArgumentResultCollector<SENDER> resolve(
        CONTEXT contextBox,
        ArgumentKey argumentKey,
        ArgumentResultCollector<SENDER> collector
    ) {
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey = IndexKey.from(contextBox, argumentKey);

        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver = resolverRegistry.getResolver(indexKey)
            .orElseThrow(() -> new IllegalStateException("No resolver for " + contextBox.getArgument()));

        return resolver.resolve(contextBox, collector.prepareCollector(contextBox));
    }

    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends ArgumentContext<DETERMINANT, EXPECTED>> resolver
    ) {
        resolverRegistry.registerResolver(indexKey, resolver);
    }

}
