package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.wrapper.ValueToWrap;

public class SuccessfulResult<EXPECTED> {

    private final ValueToWrap<EXPECTED> valueToWrap;
    private final int consumedRawArguments;

    private SuccessfulResult(ValueToWrap<EXPECTED> valueToWrap, int consumedRawArguments) {
        this.valueToWrap = valueToWrap;
        this.consumedRawArguments = consumedRawArguments;
    }

    public ValueToWrap<EXPECTED> getExpectedProvider() {
        return this.valueToWrap;
    }

    public int getConsumedRawArguments() {
        return this.consumedRawArguments;
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> of(ValueToWrap<EXPECTED> parsedArgument, int consumedRawArguments) {
        return new SuccessfulResult<>(parsedArgument, consumedRawArguments);
    }

    public static <EXPECTED> SuccessfulResult<EXPECTED> optionalArgument(ValueToWrap<EXPECTED> parsedArgument) {
        return new SuccessfulResult<>(parsedArgument, 0);
    }

}
