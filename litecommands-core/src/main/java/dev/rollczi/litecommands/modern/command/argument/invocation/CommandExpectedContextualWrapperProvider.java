package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.warpped.ExpectedContextualWrapperProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

public class CommandExpectedContextualWrapperProvider<SENDER> implements ExpectedContextualWrapperProvider<SENDER> {

    private final ArgumentService<SENDER> argumentService;
    private final WrappedExpectedContextualService wrappedExpectedContextualService;

    private ArgumentResolverContext<?> resolverContext = ArgumentResolverContext.create();

    CommandExpectedContextualWrapperProvider(ArgumentService<SENDER> argumentService, WrappedExpectedContextualService wrappedExpectedContextualService) {
        this.argumentService = argumentService;
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
    }

    @Override
    public <EXPECTED> Result<Supplier<WrappedExpectedContextual<EXPECTED>>, FailedReason> provide(Invocation<SENDER> invocation, ExpectedContextual<EXPECTED> expectedContextual) {
        if (expectedContextual instanceof ArgumentContextual) {
            ArgumentContextual<?, EXPECTED> argumentContextual = (ArgumentContextual<?, EXPECTED>) expectedContextual;

            return this.provideArgumentContextual(invocation, argumentContextual);
        }

        return this.provideContextual(invocation, expectedContextual);
    }

    private <EXPECTED> Result<Supplier<WrappedExpectedContextual<EXPECTED>>, FailedReason> provideArgumentContextual(Invocation<SENDER> invocation, ArgumentContextual<?, EXPECTED> argumentContextual) {
        ArgumentResolverContext<EXPECTED> current = this.argumentService.resolve(invocation, argumentContextual, ArgumentKey.DEFAULT, this.resolverContext);
        this.resolverContext = current;

        Option<ArgumentResult<EXPECTED>> result = current.getLastArgumentResult();

        if (result.isEmpty()) {
            throw new IllegalStateException();
        }

        ArgumentResult<EXPECTED> argumentResult = result.get();

        if (argumentResult.isFailed()) {
            Option<WrappedExpectedContextual<EXPECTED>> wrapper = this.wrappedExpectedContextualService.empty(argumentContextual);

            if (wrapper.isEmpty()) {
                return Result.error(argumentResult.getFailedReason());
            }

            return Result.ok(wrapper::get);
        }

        SuccessfulResult<EXPECTED> successfulResult = argumentResult.getSuccessfulResult();

        return Result.ok(() -> this.wrappedExpectedContextualService.wrap(successfulResult.getExpectedContextualProvider(), argumentContextual));
    }

    private <EXPECTED> Result<Supplier<WrappedExpectedContextual<EXPECTED>>, FailedReason> provideContextual(Invocation<SENDER> invocation, ExpectedContextual<EXPECTED> expectedContextual) {
        return Result.error(FailedReason.of("Not supported")); //TODO search beans and contextual beans
    }

}

