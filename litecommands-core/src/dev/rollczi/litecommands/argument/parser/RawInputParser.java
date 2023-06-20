package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.input.raw.RawInput;

public interface RawInputParser<SENDER, PARSED> extends Parser<SENDER, RawInput, PARSED> {

    default Class<RawInput> getInputType() {
        return RawInput.class;
    }

}
