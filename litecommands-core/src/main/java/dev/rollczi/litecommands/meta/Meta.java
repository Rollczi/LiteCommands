package dev.rollczi.litecommands.meta;

import java.util.Map;

public interface Meta {

    <T> void set(MetaKey<T> key, T value);

    <T> T get(MetaKey<T> key);

    Map<MetaKey<?>, Object> getMeta();

    void apply(Meta meta);

}
