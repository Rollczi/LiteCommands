package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.Nullable;

public interface MetaHolder {

    Meta meta();

    @Nullable
    MetaHolder parentMeta();

    default MetaCollector metaCollector() {
        return MetaCollector.of(this);
    }

    static MetaHolder empty() {
        return MetaHolderEmptyImpl.INSTANCE;
    }

}
