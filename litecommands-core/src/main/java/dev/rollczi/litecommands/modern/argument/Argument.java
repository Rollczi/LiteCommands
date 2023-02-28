package dev.rollczi.litecommands.modern.argument;

import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;

public interface Argument<EXPECTED> {

    String getName();

    WrapperFormat<EXPECTED> getWrapperFormat();

}
