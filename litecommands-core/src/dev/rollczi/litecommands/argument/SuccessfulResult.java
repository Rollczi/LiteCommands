package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.ValueToWrap;

public class SuccessfulResult<PARSED> {

    private final ValueToWrap<PARSED> valueToWrap;

    private SuccessfulResult(ValueToWrap<PARSED> valueToWrap) {
        this.valueToWrap = valueToWrap;
    }

    public ValueToWrap<PARSED> getExpectedProvider() {
        return this.valueToWrap;
    }

    public static <PARSED> SuccessfulResult<PARSED> of(ValueToWrap<PARSED> parsedArgument) {
        return new SuccessfulResult<>(parsedArgument);
    }

}
