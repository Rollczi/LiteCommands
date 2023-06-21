package dev.rollczi.litecommands.meta;

import java.util.Collections;
import java.util.List;

public interface MetaCollector {

    <T> List<T> collect(MetaKey<T> key);

    static MetaCollector empty() {
        return new MetaCollectorEmptyImpl();
    }

    static MetaCollector of(List<Meta> metas) {
        return new MetaCollectorImpl(metas);
    }

    static MetaCollector of(Meta meta) {
        return new MetaCollectorImpl(Collections.singletonList(meta));
    }

}
