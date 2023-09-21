package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;

public interface Requirement<T> extends MetaHolder {

    String getName();

    WrapFormat<T, ?> getWrapperFormat();

}
