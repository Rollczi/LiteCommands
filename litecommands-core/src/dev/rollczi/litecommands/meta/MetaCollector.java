package dev.rollczi.litecommands.meta;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;

public interface MetaCollector {

    <T> List<T> collect(MetaKey<T> key);

    @ApiStatus.Experimental
    <T> List<T> collectReverse(MetaKey<T> key);

    <T> Iterable<T> iterable(MetaKey<T> key);

    <T> T findFirst(MetaKey<T> key);

    <T> T findFirst(MetaKey<T> key, T defaultValue);

    <T> T findLast(MetaKey<T> key);

    <T> T findLast(MetaKey<T> key, T defaultValue);

    static MetaCollector of(MetaHolder metaHolder) {
        return new MetaHolderCollectorImpl(metaHolder);
    }

}
