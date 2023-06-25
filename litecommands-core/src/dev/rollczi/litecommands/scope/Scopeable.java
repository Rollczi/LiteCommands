package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.Collection;

public interface Scopeable extends MetaHolder {

    Collection<String> names();

}
