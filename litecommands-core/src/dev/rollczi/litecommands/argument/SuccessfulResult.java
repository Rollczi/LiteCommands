package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.ValueToWrap;

public class SuccessfulResult<PARSED> {

    private final ValueToWrap<PARSED> valueToWrap;
    private final int consumedRawArguments;

    private SuccessfulResult(ValueToWrap<PARSED> valueToWrap, int consumedRawArguments) {
        this.valueToWrap = valueToWrap;
        this.consumedRawArguments = consumedRawArguments;
    }

    public ValueToWrap<PARSED> getExpectedProvider() {
        return this.valueToWrap;
    }

    public int getConsumedRawArguments() {
        return this.consumedRawArguments;
    }

    public static <PARSED> SuccessfulResult<PARSED> of(ValueToWrap<PARSED> parsedArgument, int consumedRawArguments) {
        return new SuccessfulResult<>(parsedArgument, consumedRawArguments);
    }

    public static <PARSED> SuccessfulResult<PARSED> optionalArgument(ValueToWrap<PARSED> parsedArgument) {
        return new SuccessfulResult<>(parsedArgument, 0);
    }

}
