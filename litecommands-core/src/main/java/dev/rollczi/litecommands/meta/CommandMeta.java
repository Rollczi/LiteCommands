package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface CommandMeta {

    CommandKey<List<String>> PERMISSIONS = CommandKey.of("permissions", CommandMetaType.list(), Collections.emptyList());
    CommandKey<List<String>> PERMISSIONS_EXCLUDED = CommandKey.of("permissions-excluded", CommandMetaType.list(), Collections.emptyList());
    CommandKey<Boolean> NATIVE_PERMISSIONS = CommandKey.of("native-permissions", Boolean.class, false);
    CommandKey<Boolean> ASYNCHRONOUS = CommandKey.of("asynchronous", Boolean.class, false);

    CommandMeta EMPTY_IMMUTABLE = new CommandMetaEmptyImpl();

    <T> CommandMeta put(CommandKey<T> key, T value);

    <E> CommandMeta appendToList(CommandKey<List<E>> key, E element);

    <E> CommandMeta appendToSet(CommandKey<Set<E>> key, E element);

    <T> CommandMeta remove(CommandKey<T> key);

    List<String> permissions();

    List<String> permissionsExcluded();

    @NotNull <T> T get(CommandKey<T> key);

    CommandMeta clear();

    CommandMeta apply(CommandMeta meta);

    CommandMeta copy();

    Collection<CommandKey<?>> getKeys();

    static CommandMeta create() {
        return new CommandMetaImpl();
    }

}
