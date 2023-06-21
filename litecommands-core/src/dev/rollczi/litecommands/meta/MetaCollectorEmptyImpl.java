package dev.rollczi.litecommands.meta;

import java.util.Collections;
import java.util.List;

class MetaCollectorEmptyImpl implements MetaCollector {

    @Override
    public <T> List<T> collect(MetaKey<T> key) {
        return Collections.emptyList();
    }

}
