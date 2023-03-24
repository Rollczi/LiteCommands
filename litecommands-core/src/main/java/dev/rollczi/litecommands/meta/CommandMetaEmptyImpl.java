package dev.rollczi.litecommands.meta;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

class CommandMetaEmptyImpl implements CommandMeta {

    @Override
    public <T> CommandMeta put(CommandKey<T> key, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> CommandMeta appendToList(CommandKey<List<E>> key, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> CommandMeta appendToSet(CommandKey<Set<E>> key, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> CommandMeta remove(CommandKey<T> key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> permissions() {
        return Collections.emptyList();
    }

    @Override
    public List<String> permissionsExcluded() {
        return Collections.emptyList();
    }

    @Override
    public <T> T get(CommandKey<T> key) {
        throw new NoSuchElementException();
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
