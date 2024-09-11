package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.Nullable;

class MetaHolderEmptyImpl implements MetaHolder {

    static final MetaHolder INSTANCE = new MetaHolderEmptyImpl();

    @Override
    public Meta meta() {
        return Meta.EMPTY_META;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return null;
    }

}
