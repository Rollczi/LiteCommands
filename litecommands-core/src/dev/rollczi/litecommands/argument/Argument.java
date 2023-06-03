package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.WrapFormat;

public interface Argument<PARSED> {

    String getName();

    WrapFormat<PARSED, ?> getWrapperFormat();

}