package dev.rollczi.litecommands.wrapper;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface ValueToWrap<EXPECTED> extends Supplier<EXPECTED> {

    @Override
    @Nullable
    EXPECTED get();

}
