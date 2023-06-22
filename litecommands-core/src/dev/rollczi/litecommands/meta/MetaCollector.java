package dev.rollczi.litecommands.meta;

import java.util.List;

public interface MetaCollector {

    <T> List<T> collect(MetaKey<T> key);

    static MetaCollector of(MetaHolder metaHolder) {
        return new MetaHolderCollectorImpl(metaHolder);
    }

}
