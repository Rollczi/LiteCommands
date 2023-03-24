package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.WrapperFormat;

public interface Argument<EXPECTED> {

    String getName();

    WrapperFormat<EXPECTED> getWrapperFormat();

}
