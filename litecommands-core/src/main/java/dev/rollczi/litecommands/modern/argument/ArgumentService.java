package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.command.ExecutableArgument;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Option;

import java.util.List;

public class ArgumentService<SENDER> {

    private final ArgumentResolverRegistry<SENDER> resolverRegistry = new ArgumentResolverRegistryImpl<>();

    public <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> ArgumentResolverContext<EXPECTED> resolve(
        Invocation<SENDER> invocation,
        ARGUMENT context,
        ArgumentKey argumentKey,
        ArgumentResolverContext<?> resolverContext
    ) {
        ArgumentResolverRegistry.IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey = ArgumentResolverRegistry.IndexKey.from(context, argumentKey);

        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver = this.resolverRegistry.getResolver(indexKey)
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


    public <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> ArgumentResolverContext<EXPECTED> resolve(
        Invocation<SENDER> invocation,
        ExecutableArgument<SENDER, DETERMINANT, EXPECTED, ARGUMENT> executableArgument,
        ArgumentResolverContext<?> resolverContext
    ) {
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver = executableArgument.getResolver();

        Option<? extends ArgumentResult<?>> lastResult = resolverContext.getLastArgumentResult();

        if (lastResult.isPresent() && lastResult.get().isFailed()) {
            throw new IllegalStateException("Cannot resolve arguments when last argument is failed");
        }

        int lastResolvedRawArgument = resolverContext.getLastResolvedRawArgument();

        List<String> rawArguments = invocation.argumentsList();
        int lastRequiredArgument = lastResolvedRawArgument + resolver.getRange().getMin();

        if (lastRequiredArgument > rawArguments.size()) {
            return resolverContext.withFailure(); // za mało argumentów
        }

        List<String> arguments = rawArguments.subList(lastResolvedRawArgument, lastRequiredArgument);
        ArgumentResult<EXPECTED> result = resolver.parse(invocation, arguments, executableArgument.getContextual());

        if (result.isFailed()) {
            return resolverContext.withFailure(result);
        }

        SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

        return resolverContext.with(successfulResult.getConsumedRawArguments(), result);
    }

    public <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> void registerResolver(
        ArgumentResolverRegistry.IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends Argument<DETERMINANT, EXPECTED>> resolver
    ) {
        this.resolverRegistry.registerResolver(indexKey, resolver);
    }

    public ArgumentResolverRegistry<SENDER> getResolverRegistry() {
        return resolverRegistry;
    }
}
