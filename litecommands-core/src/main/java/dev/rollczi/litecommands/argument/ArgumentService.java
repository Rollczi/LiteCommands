package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import panda.std.Option;

import java.util.List;

public class ArgumentService<SENDER> {

    private final ArgumentResolverRegistry<SENDER> resolverRegistry = new ArgumentResolverRegistryImpl<>();

    public <EXPECTED> ArgumentResolverContext<EXPECTED> resolve(
        Invocation<SENDER> invocation,
        PreparedArgument<SENDER, EXPECTED> preparedArgument,
        ArgumentResolverContext<?> resolverContext
    ) {

        Option<? extends PreparedArgumentResult<?>> lastResult = resolverContext.getLastArgumentResult();

        if (lastResult.isPresent() && lastResult.get().isFailed()) {
            throw new IllegalStateException("Cannot resolve arguments when last argument is failed");
        }

        Range range = preparedArgument.getRange();
        int lastResolvedRawArgument = resolverContext.getLastResolvedRawArgument();

        List<String> rawArguments = invocation.argumentsList();
        int minArguments = range.getMin();
        int maxArguments = range.getMax() == Integer.MAX_VALUE
            ? rawArguments.size()
            : lastResolvedRawArgument + range.getMax();

        maxArguments = Math.min(maxArguments, rawArguments.size());

        if (minArguments > rawArguments.size()) {
            return resolverContext.withFailure(PreparedArgumentResult.failed(InvalidUsage.Cause.TOO_FEW_ARGUMENTS));
        }

        List<String> arguments = rawArguments.subList(lastResolvedRawArgument, maxArguments);
        PreparedArgumentResult<EXPECTED> result = preparedArgument.resolve(invocation, arguments);

        if (result.isFailed()) {
            return resolverContext.withFailure(result);
        }

        PreparedArgumentResult.Success<EXPECTED> success = result.getSuccess();

        return resolverContext.with(success.getConsumedRawArguments(), result);
    }

    public <EXPECTED, ARGUMENT extends Argument<EXPECTED>> void registerResolver(
        ArgumentResolverRegistry.IndexKey<EXPECTED, ARGUMENT> indexKey,
        ArgumentParser<SENDER, EXPECTED, ? extends Argument<EXPECTED>> resolver
    ) {
        this.resolverRegistry.registerResolver(indexKey, resolver);
    }

    public ArgumentResolverRegistry<SENDER> getResolverRegistry() {
        return resolverRegistry;
    }

}
