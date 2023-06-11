package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.input.RawInput;

public interface ArgumentRawInputParser<SENDER, PARSED> extends ArgumentParser<SENDER, RawInput, PARSED> {

    default Class<RawInput> getInputType() {
        return RawInput.class;
    }

}
