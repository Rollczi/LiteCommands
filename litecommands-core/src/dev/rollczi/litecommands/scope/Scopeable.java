package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaCollector;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.Collection;
import java.util.function.Consumer;

public interface Scopeable extends MetaHolder {

    Collection<String> names();

    @Override
    default void editMeta(Consumer<Meta> operator) {
        operator.accept(meta());
    }

    @Override
    Meta meta();

    @Override
    MetaCollector metaCollector();

}
