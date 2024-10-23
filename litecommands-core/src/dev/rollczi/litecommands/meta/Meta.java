package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.cooldown.CooldownContext;
import dev.rollczi.litecommands.priority.PriorityLevel;
import dev.rollczi.litecommands.scheduler.SchedulerPoll;
import dev.rollczi.litecommands.strict.StrictMode;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.requirement.RequirementValidator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

@SuppressWarnings("rawtypes")
public interface Meta {

    MetaKey<List<String>> DESCRIPTION = MetaKey.of("description", MetaType.list(), Collections.emptyList());
    MetaKey<List<String>> PERMISSIONS = MetaKey.of("permissions", MetaType.list(), Collections.emptyList());
    MetaKey<PriorityLevel> PRIORITY = MetaKey.of("priority", PriorityLevel.class, PriorityLevel.NORMAL);
    MetaKey<Boolean> NATIVE_PERMISSIONS = MetaKey.of("native-permissions", Boolean.class, false);
    MetaKey<SchedulerPoll> POLL_TYPE = MetaKey.of("poll-type", SchedulerPoll.class, SchedulerPoll.MAIN);
    @Deprecated
    MetaKey<ArgumentKey> ARGUMENT_KEY = MetaKey.of("argument-key", ArgumentKey.class, ArgumentKey.of());
    MetaKey<List<Class>> COMMAND_ORIGIN_TYPE = MetaKey.of("command-origin-class", MetaType.list(), Collections.emptyList());
    MetaKey<List<Class<? extends Validator<?>>>> VALIDATORS = MetaKey.of("validators", MetaType.list(), Collections.emptyList());
    MetaKey<List<RequirementValidator<?, ?>>> REQUIREMENT_VALIDATORS = MetaKey.of("requirement-validators", MetaType.list(), Collections.emptyList());
    MetaKey<CooldownContext> COOLDOWN = MetaKey.of("cooldown", CooldownContext.class);
    MetaKey<StrictMode> STRICT_MODE = MetaKey.of("strict-mode", StrictMode.class, StrictMode.DEFAULT);

    /**
     * @deprecated Use AnnotationsMeta#REQUIREMENT_PARAMETER instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "3.12.0")
    MetaKey<Parameter> REQUIREMENT_PARAMETER = MetaKey.of("requirement-parameter", Parameter.class);

    Meta EMPTY_META = new MetaEmptyImpl();

    @NotNull <T> T get(MetaKey<T> key);

    @Contract("_, !null -> !null; _, null -> _")
    <T> T get(MetaKey<T> key, T defaultValue);

    <T> Meta put(MetaKey<T> key, T value);

    @ApiStatus.Experimental
    <T> Meta edit(MetaKey<T> key, UnaryOperator<T> operator);

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

    /**
     * @deprecated Use {@link #putAll(Meta)} instead
     */
    @Deprecated
    default Meta apply(Meta meta) {
        return putAll(meta);
    }

    Meta putAll(Meta meta);

    Meta copy();

    Collection<MetaKey<?>> getKeys();

    static Meta create() {
        return new MetaImpl();
    }

}
