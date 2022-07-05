package dev.rollczi.litecommands.meta;

import java.util.Map;

public interface Meta {

    <T> Meta set(MetaKey<T> key, T value);

    <T> T get(MetaKey<T> key);

    Map<MetaKey<?>, Object> getMeta();

    Meta apply(Meta meta);

}
