package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.MetaCollector;

import java.util.Collection;

public interface Scopeable {

    Collection<String> names();

    CommandMeta meta();

    MetaCollector metaCollector();

}
