package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.ArgumentResolverContext;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.ArgumentService;
import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.argument.PreparedArgument;
import dev.rollczi.litecommands.argument.SuccessfulResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

class PreparedArgumentIteratorImpl<SENDER> implements PreparedArgumentIterator<SENDER> {

    private final ArgumentService<SENDER> argumentService;
    private final WrappedExpectedService wrappedExpectedService;

    private ArgumentResolverContext<?> resolverContext;

    PreparedArgumentIteratorImpl(
        ArgumentService<SENDER> argumentService,
        WrappedExpectedService wrappedExpectedService,
        int childIndex
    ) {
        this.argumentService = argumentService;
        this.wrappedExpectedService = wrappedExpectedService;
        this.resolverContext = ArgumentResolverContext.create(childIndex);
    }

    @Override
    public <EXPECTED> Result<Supplier<WrappedExpected<EXPECTED>>, FailedReason> resolveNext(Invocation<SENDER> invocation, PreparedArgument<SENDER, EXPECTED> preparedArgument) {
        ArgumentResolverContext<EXPECTED> current = this.argumentService.resolve(invocation, preparedArgument, this.resolverContext);
        this.resolverContext = current;

        Option<ArgumentResult<EXPECTED>> result = current.getLastArgumentResult();

        if (result.isEmpty()) {
            throw new IllegalStateException();
        }

        ArgumentResult<EXPECTED> argumentResult = result.get();

        if (argumentResult.isFailed()) {
            Option<WrappedExpected<EXPECTED>> wrapper = this.wrappedExpectedService.empty(preparedArgument.getWrapperFormat());

            if (wrapper.isEmpty()) {
                return Result.error(argumentResult.getFailedReason());
            }

            return Result.ok(wrapper::get);
        }

        SuccessfulResult<EXPECTED> successfulResult = argumentResult.getSuccessfulResult();

        return Result.ok(() -> this.wrappedExpectedService.wrap(successfulResult.getExpectedProvider(), preparedArgument.getWrapperFormat()));
    }

}

