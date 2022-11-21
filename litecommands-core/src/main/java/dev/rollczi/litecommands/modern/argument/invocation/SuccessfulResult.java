package dev.rollczi.litecommands.modern.argument.invocation;

import jdk.internal.jline.internal.Nullable;

import java.util.Optional;

public class SuccessfulResult<EXPECTED> {

    private final @Nullable EXPECTED parsedArgument;
    private final int consumedRawArguments;

    private SuccessfulResult(EXPECTED parsedArgument, int consumedRawArguments) {
        this.parsedArgument = parsedArgument;
        this.consumedRawArguments = consumedRawArguments;
    }

    public Optional<EXPECTED> getParsedArgument() {
        return Optional.ofNullable(parsedArgument);
    }

    public int getConsumedRawArguments() {
        return consumedRawArguments;
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> of(EXPECTED parsedArgument, int consumedRawArguments) {
        return new SuccessfulResult<>(parsedArgument, consumedRawArguments);
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> optionalArgument(EXPECTED parsedArgument) {
        return new SuccessfulResult<>(parsedArgument, 0);
    }

}
