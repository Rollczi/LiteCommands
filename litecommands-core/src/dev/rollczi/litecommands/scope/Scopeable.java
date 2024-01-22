package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.Collection;
import org.jetbrains.annotations.Unmodifiable;

public interface Scopeable extends MetaHolder {

    @Unmodifiable
    Collection<String> names();

}
