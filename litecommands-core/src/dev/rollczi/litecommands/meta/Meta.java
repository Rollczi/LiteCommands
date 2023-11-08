package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.requirment.RequirementValidator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

@SuppressWarnings("rawtypes")
public interface Meta {

    MetaKey<String> DESCRIPTION = MetaKey.of("description", String.class, "none", true);
    MetaKey<List<String>> PERMISSIONS = MetaKey.of("permissions", MetaType.list(), Collections.emptyList(), true);
    MetaKey<Boolean> NATIVE_PERMISSIONS = MetaKey.of("native-permissions", Boolean.class, false, true);
    MetaKey<SchedulerPoll> POLL_TYPE = MetaKey.of("poll-type", SchedulerPoll.class, SchedulerPoll.MAIN, true);
    MetaKey<String> ARGUMENT_KEY = MetaKey.of("argument-key", String.class);
    MetaKey<Class> COMMAND_ORIGIN_TYPE = MetaKey.of("command-origin-class", Class.class);
    MetaKey<List<Class<? extends Validator<?>>>> VALIDATORS = MetaKey.of("validators", MetaType.list(), Collections.emptyList(), true);
    MetaKey<List<RequirementValidator<?, ?>>> REQUIREMENT_VALIDATORS = MetaKey.of("requirement-validators", MetaType.list(), Collections.emptyList(), true);

    Meta EMPTY_META = new MetaEmptyImpl();

    @NotNull <T> T get(MetaKey<T> key);

    @NotNull <T> T get(MetaKey<T> key, T defaultValue);

    <T> Meta put(MetaKey<T> key, T value);

    <T> Meta remove(MetaKey<T> key);

    Meta clear();

    boolean has(MetaKey<?> key);

    default <E> MetaListEditor<E> listEditor(MetaKey<List<E>> key) {
        return new MetaListEditor<>(this.get(key), this, key);
    }

    default <E> Meta list(MetaKey<List<E>> key, UnaryOperator<MetaListEditor<E>> operator) {
        MetaListEditor<E> editor = listEditor(key);

        return operator.apply(editor).apply();
    }

    Meta apply(Meta meta);

    Meta copy();

    default Meta copyToFastUse() {
        return copy();
    }

    Collection<MetaKey<?>> getKeys();

    static Meta create() {
        return new MetaImpl();
    }

}
