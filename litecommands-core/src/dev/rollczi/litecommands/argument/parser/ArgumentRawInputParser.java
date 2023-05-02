package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.input.RawInput;
import dev.rollczi.litecommands.range.Rangeable;

public interface ArgumentRawInputParser<SENDER, PARSED> extends ArgumentParser<SENDER, RawInput, PARSED>, Rangeable {

    default Class<RawInput> getInputType() {
        return RawInput.class;
    }

}
