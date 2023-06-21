package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.scheduler.SchedulerPollType;
import dev.rollczi.litecommands.validator.Validator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Meta {

    MetaKey<String> DESCRIPTION = MetaKey.of("description", String.class, "none");
    MetaKey<List<String>> PERMISSIONS = MetaKey.of("permissions", MetaType.list(), Collections.emptyList());
    MetaKey<List<String>> PERMISSIONS_EXCLUDED = MetaKey.of("permissions-excluded", MetaType.list(), Collections.emptyList());
    MetaKey<Boolean> NATIVE_PERMISSIONS = MetaKey.of("native-permissions", Boolean.class, false);
    MetaKey<SchedulerPollType> POLL_TYPE = MetaKey.of("poll-type", SchedulerPollType.class, SchedulerPollType.SYNC);
    MetaKey<Class> COMMAND_ORIGIN_TYPE = MetaKey.of("command-origin-class", Class.class);
    MetaKey<List<Class<? extends Validator<?>>>> VALIDATORS = MetaKey.of("validators", MetaType.list(), Collections.emptyList());

    Meta EMPTY_META = new MetaEmptyImpl();

    @NotNull <T> T get(MetaKey<T> key);

    <T> Meta put(MetaKey<T> key, T value);

    <T> Meta remove(MetaKey<T> key);

    Meta clear();

    boolean has(MetaKey<?> key);

    default <E> MetaListEditor<E> listEditor(MetaKey<List<E>> key) {
        return new MetaListEditor<>(this.get(key), this, key);
    }

    Meta apply(Meta meta);

    Meta copy();

    Collection<MetaKey<?>> getKeys();

    static Meta create() {
        return new MetaImpl();
    }

}
