package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.Nullable;

public class MetaHolderImpl implements MetaHolder {

    private final Meta meta;

    public MetaHolderImpl(Meta meta) {
        this.meta = meta;
    }

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return null;
    }

}
