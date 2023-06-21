package dev.rollczi.litecommands.meta;

import java.util.function.Consumer;

public interface MetaHolder {

    void editMeta(Consumer<Meta> operator);

    Meta meta();

    MetaCollector metaCollector();

}
