package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.ArgumentResolverContext;
import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.argument.PreparedArgumentResult;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.function.Supplier;

class PreparedArgumentIteratorImpl<SENDER> implements PreparedArgumentIterator<SENDER> {

    private ArgumentResolverContext<?> resolverContext;

    PreparedArgumentIteratorImpl(
        int childIndex
    ) {
        this.resolverContext = ArgumentResolverContext.create(childIndex);
    }

    @Override
    public <EXPECTED> Result<Supplier<WrappedExpected<EXPECTED>>, FailedReason> resolveNext(Invocation<SENDER> invocation, PreparedArgument<SENDER, EXPECTED> preparedArgument) {
        ArgumentResolverContext<EXPECTED> current = resolve(invocation, preparedArgument, this.resolverContext);
        this.resolverContext = current;

        Option<PreparedArgumentResult<EXPECTED>> result = current.getLastArgumentResult();

        if (result.isEmpty()) {
            throw new IllegalStateException();
        }

        PreparedArgumentResult<EXPECTED> argumentResult = result.get();

        if (argumentResult.isFailed()) { // TODO add option disable strict mode and use empty wrapper
            Option<WrappedExpected<EXPECTED>> wrapper = Option.none();

            if (wrapper.isEmpty()) {
                return Result.error(argumentResult.getFailedReason());
            }

            return Result.ok(wrapper::get);
        }

        PreparedArgumentResult.Success<EXPECTED> successfulResult = argumentResult.getSuccess();

        return Result.ok(successfulResult.getWrappedExpected());
    }

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

}

