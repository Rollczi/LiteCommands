package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;

import java.util.function.Supplier;

public class SuccessfulResult<EXPECTED> {

    private final ExpectedContextualProvider<EXPECTED> expectedContextualProvider;
    private final int consumedRawArguments;

    private SuccessfulResult(ExpectedContextualProvider<EXPECTED> expectedContextualProvider, int consumedRawArguments) {
        this.expectedContextualProvider = expectedContextualProvider;
        this.consumedRawArguments = consumedRawArguments;
    }

    public ExpectedContextualProvider<EXPECTED> getExpectedContextualProvider() {
        return expectedContextualProvider;
    }

    public int getConsumedRawArguments() {
        return consumedRawArguments;
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> of(ExpectedContextualProvider<EXPECTED> parsedArgument, int consumedRawArguments) {
        return new SuccessfulResult<>(parsedArgument, consumedRawArguments);
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> optionalArgument(ExpectedContextualProvider<EXPECTED> parsedArgument) {
        return new SuccessfulResult<>(parsedArgument, 0);
    }

}
