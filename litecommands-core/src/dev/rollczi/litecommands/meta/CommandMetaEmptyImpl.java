package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

class CommandMetaEmptyImpl implements CommandMeta {

    @Override
    public <T> @NotNull T get(CommandKey<T> key) {
        throw new NoSuchElementException();
    }

    @Override
    public <T> CommandMeta put(CommandKey<T> key, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> CommandMeta remove(CommandKey<T> key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommandMeta clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommandMeta apply(CommandMeta meta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommandMeta copy() {
        return this;
    }

    @Override
    public Collection<CommandKey<?>> getKeys() {
        return Collections.emptyList();
    }

}
