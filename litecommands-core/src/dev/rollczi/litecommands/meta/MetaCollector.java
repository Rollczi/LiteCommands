package dev.rollczi.litecommands.meta;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface MetaCollector {

    <T> List<T> collect(MetaKey<T> key);

    @ApiStatus.Experimental
    <T> List<T> collectReverse(MetaKey<T> key);

    <T> Iterable<T> iterable(MetaKey<T> key);

    <T> T findFirst(MetaKey<T> key);

    @Nullable
    @Contract("_,null -> _")
    <T> T findFirst(MetaKey<T> key, @Nullable T defaultValue);

    <T> T findLast(MetaKey<T> key);

    @Nullable
    @Contract("_,null -> _; _,!null -> !null")
    <T> T findLast(MetaKey<T> key, @Nullable T defaultValue);

    static MetaCollector of(MetaHolder metaHolder) {
        return new MetaHolderCollectorImpl(metaHolder);
    }

}
