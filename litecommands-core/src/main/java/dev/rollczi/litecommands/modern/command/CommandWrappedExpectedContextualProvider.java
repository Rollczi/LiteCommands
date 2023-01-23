package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
import dev.rollczi.litecommands.modern.command.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

public class CommandWrappedExpectedContextualProvider<SENDER> implements WrappedExpectedContextualProvider<SENDER> {

    private final ArgumentService<SENDER> argumentService;
    private final WrappedExpectedContextualService wrappedExpectedContextualService;
    private final BindRegistry<SENDER> bindRegistry;

    private ArgumentResolverContext<?> resolverContext = ArgumentResolverContext.create();

    public CommandWrappedExpectedContextualProvider(ArgumentService<SENDER> argumentService, WrappedExpectedContextualService wrappedExpectedContextualService, BindRegistry<SENDER> bindRegistry) {
        this.argumentService = argumentService;
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
        this.bindRegistry = bindRegistry;
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

