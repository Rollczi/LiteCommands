package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.validator.CommandValidator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface CommandMeta {

    CommandKey<List<String>> PERMISSIONS = CommandKey.of("permissions", CommandMetaType.list(), Collections.emptyList());
    CommandKey<List<String>> PERMISSIONS_EXCLUDED = CommandKey.of("permissions-excluded", CommandMetaType.list(), Collections.emptyList());
    CommandKey<Boolean> NATIVE_PERMISSIONS = CommandKey.of("native-permissions", Boolean.class, false);
    CommandKey<Boolean> ASYNCHRONOUS = CommandKey.of("asynchronous", Boolean.class, false);
    CommandKey<List<Class<? extends CommandValidator<?>>>> VALIDATORS = CommandKey.of("validators", CommandMetaType.list(), Collections.emptyList());

    CommandMeta EMPTY_IMMUTABLE = new CommandMetaEmptyImpl();

    @NotNull <T> T get(CommandKey<T> key);

    <T> CommandMeta put(CommandKey<T> key, T value);

    <T> CommandMeta remove(CommandKey<T> key);

    CommandMeta clear();

    default <E> CommandMetaListEditor<E> listEditor(CommandKey<List<E>> key) {
        return new CommandMetaListEditor<>(this.get(key), this, key);
    }

    CommandMeta apply(CommandMeta meta);

    CommandMeta copy();

    Collection<CommandKey<?>> getKeys();

    static CommandMeta create() {
        return new CommandMetaImpl();
    }

}
