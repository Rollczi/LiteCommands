package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverContext;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.argument.SuccessfulResult;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.ExpectedContextualConverter;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

public class CommandContextualConverter<SENDER> implements ExpectedContextualConverter<SENDER> {

    private final ArgumentService<SENDER> argumentService;
    private final BindRegistry<SENDER> bindRegistry;
    private final WrappedExpectedContextualService wrappedExpectedContextualService;

    private ArgumentResolverContext<?> resolverContext = ArgumentResolverContext.create();

    public CommandContextualConverter(
        ArgumentService<SENDER> argumentService,
        BindRegistry<SENDER> bindRegistry,
        WrappedExpectedContextualService wrappedExpectedContextualService
    ) {
        this.argumentService = argumentService;
        this.bindRegistry = bindRegistry;
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

