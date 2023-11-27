package dev.rollczi.litecommands.wrapper;

import org.jetbrains.annotations.Nullable;

public interface Wrap<PARSED> {

    @Nullable
    Object unwrap();

    Class<PARSED> getParsedType();

}
