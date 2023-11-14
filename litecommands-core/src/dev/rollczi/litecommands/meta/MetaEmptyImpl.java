package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

class MetaEmptyImpl implements Meta {

    @Override
    public <T> @NotNull T get(MetaKey<T> key) {
        throw new NoSuchElementException();
    }

    @Override
    public <T> @NotNull T get(MetaKey<T> key, T defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> Meta put(MetaKey<T> key, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Meta remove(MetaKey<T> key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Meta clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean has(MetaKey<?> key) {
        return false;
    }

    @Override
    public Meta apply(Meta meta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Meta copy() {
        return this;
    }

    @Override
    public Collection<MetaKey<?>> getKeys() {
        return Collections.emptyList();
    }

}
