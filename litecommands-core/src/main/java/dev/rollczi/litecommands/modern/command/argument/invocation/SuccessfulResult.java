package dev.rollczi.litecommands.modern.command.argument.invocation;

import java.util.function.Supplier;

public class SuccessfulResult<EXPECTED> {

    private final Supplier<EXPECTED> parsedArgument;
    private final int consumedRawArguments;

    private SuccessfulResult(Supplier<EXPECTED> parsedArgument, int consumedRawArguments) {
        this.parsedArgument = parsedArgument;
        this.consumedRawArguments = consumedRawArguments;
    }

    public Supplier<EXPECTED> getParsedArgumentProvider() {
        return parsedArgument;
    }

    public int getConsumedRawArguments() {
        return consumedRawArguments;
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> of(Supplier<EXPECTED> parsedArgument, int consumedRawArguments) {
        return new SuccessfulResult<>(parsedArgument, consumedRawArguments);
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> optionalArgument(Supplier<EXPECTED> parsedArgument) {
        return new SuccessfulResult<>(parsedArgument, 0);
    }

}
