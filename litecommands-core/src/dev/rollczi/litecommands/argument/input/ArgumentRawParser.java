package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.range.Rangeable;

public interface ArgumentRawParser<SENDER, PARSED> extends ArgumentParser<SENDER, RawInput, PARSED>, Rangeable {

    default Class<RawInput> getInputType() {
        return RawInput.class;
    }

}
