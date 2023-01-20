package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry.IndexKey;
import panda.std.Option;

import java.util.List;

public class ArgumentService<SENDER> {

    private final ArgumentResolverRegistry<SENDER> resolverRegistry = new ArgumentResolverRegistryImpl<>();

    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>> ArgumentResolverContext<EXPECTED> resolve(
        Invocation<SENDER> invocation,
        CONTEXT context,
        ArgumentKey argumentKey,
        ArgumentResolverContext<?> resolverContext
    ) {
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey = IndexKey.from(context, argumentKey);

        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> resolver = this.resolverRegistry.getResolver(indexKey)
            .orElseThrow(() -> new IllegalStateException("No resolver for " + context.getDeterminantType()));


        Option<? extends ArgumentResult<?>> lastResult = resolverContext.getLastArgumentResult();

        if (lastResult.isPresent() && lastResult.get().isFailed()) {
            throw new IllegalStateException("Cannot resolve arguments when last argument is failed");
        }

        int lastResolvedRawArgument = resolverContext.getLastResolvedRawArgument();

        List<String> rawArguments = invocation.argumentsList();
        int lastRequiredArgument = lastResolvedRawArgument + resolver.getRange().getMin();

        if (lastRequiredArgument > rawArguments.size()) {
            return resolverContext.withFailure();
        }

        List<String> arguments = rawArguments.subList(lastResolvedRawArgument, lastRequiredArgument);
        ArgumentResult<EXPECTED> result = resolver.parse(invocation, arguments, context);

        if (result.isFailed()) {
            return resolverContext.withFailure(result);
        }

        SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

        return resolverContext.with(successfulResult.getConsumedRawArguments(), result);
    }

    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends ArgumentContextual<DETERMINANT, EXPECTED>> resolver
    ) {
        this.resolverRegistry.registerResolver(indexKey, resolver);
    }

}
