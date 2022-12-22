package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry.IndexKey;

public class ArgumentService<SENDER> {

    private final ArgumentResolverRegistry<SENDER> resolverRegistry = new ArgumentResolverRegistryImpl<>();

    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> ArgumentResultCollector<SENDER> resolve(
        CONTEXT context,
        ArgumentKey argumentKey,
        ArgumentResultCollector<SENDER> collector
    ) {
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey = IndexKey.from(context, argumentKey);

        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver = resolverRegistry.getResolver(indexKey)
            .orElseThrow(() -> new IllegalStateException("No resolver for " + context.getArgument()));

        return resolver.resolve(context, collector.prepareCollector(context));
    }

    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends ArgumentContext<DETERMINANT, EXPECTED>> resolver
    ) {
        resolverRegistry.registerResolver(indexKey, resolver);
    }

}
