package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Option;

import java.util.List;

public class ArgumentService<SENDER> {

    private final ArgumentResolverRegistry<SENDER> resolverRegistry = new ArgumentResolverRegistryImpl<>();

    public <EXPECTED, ARGUMENT extends Argument<EXPECTED>> ArgumentResolverContext<EXPECTED> resolve(
        Invocation<SENDER> invocation,
        ARGUMENT argument,
        String customKey,
        ArgumentResolverContext<?> resolverContext
    ) {
        ArgumentResolverRegistry.IndexKey<EXPECTED, ARGUMENT> indexKey = ArgumentResolverRegistry.IndexKey.from(argument, customKey);

        ArgumentParser<SENDER, EXPECTED, ARGUMENT> resolver = this.resolverRegistry.getResolver(indexKey)
            .orElseThrow(() -> new IllegalStateException("Resolver for argument '" + argument.getName() + "' is not exist!"));


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
        ArgumentResult<EXPECTED> result = resolver.parse(invocation, argument, arguments);

        if (result.isFailed()) {
            return resolverContext.withFailure(result);
        }

        SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

        return resolverContext.with(successfulResult.getConsumedRawArguments(), result);
    }


    public <EXPECTED> ArgumentResolverContext<EXPECTED> resolve(
        Invocation<SENDER> invocation,
        PreparedArgument<SENDER, EXPECTED> preparedArgument,
        ArgumentResolverContext<?> resolverContext
    ) {

        Option<? extends ArgumentResult<?>> lastResult = resolverContext.getLastArgumentResult();

        if (lastResult.isPresent() && lastResult.get().isFailed()) {
            throw new IllegalStateException("Cannot resolve arguments when last argument is failed");
        }

        int lastResolvedRawArgument = resolverContext.getLastResolvedRawArgument();

        List<String> rawArguments = invocation.argumentsList();
        int lastRequiredArgument = lastResolvedRawArgument + preparedArgument.getRange().getMin();

        if (lastRequiredArgument > rawArguments.size()) {
            return resolverContext.withFailure(); // za mało argumentów
        }

        List<String> arguments = rawArguments.subList(lastResolvedRawArgument, lastRequiredArgument);
        ArgumentResult<EXPECTED> result = preparedArgument.resolve(invocation, arguments);

        if (result.isFailed()) {
            return resolverContext.withFailure(result);
        }

        SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

        return resolverContext.with(successfulResult.getConsumedRawArguments(), result);
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
