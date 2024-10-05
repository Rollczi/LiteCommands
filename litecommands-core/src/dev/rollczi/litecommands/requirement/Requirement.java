package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.reflect.type.TypeToken;

public interface Requirement<T> extends MetaHolder {

    String getName();

    TypeToken<T> getType();

}
