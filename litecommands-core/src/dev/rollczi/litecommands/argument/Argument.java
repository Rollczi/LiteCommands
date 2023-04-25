package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.WrapperFormat;

public interface Argument<PARSED> {

    String getName();

    WrapperFormat<PARSED, ?> getWrapperFormat();

}