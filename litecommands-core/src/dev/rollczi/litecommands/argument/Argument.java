package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.priority.PrioritizedList;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;

import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;

public interface Argument<T> extends Requirement<T> {

    String getName();

    ArgumentKey getKey();

    Optional<ParseResult<T>> getDefaultValue();

    @Deprecated
    default Optional<ParseResult<T>> defaultValue() {
        return getDefaultValue();
    }

    boolean hasDefaultValue();

    @ApiStatus.Experimental
    <P> Optional<P> getProfile(ArgumentProfileNamespace<P> key);

    @ApiStatus.Experimental
    PrioritizedList<ArgumentProfile<?>> getProfiles();

    /**
     * Create a child of the current argument.
     * This is useful when resolver handles a parametrized type for class such as Optional, List, CompletableFuture, etc.
     */
    @ApiStatus.Experimental
    <NEW> Argument<NEW> child(TypeToken<NEW> type);

    @ApiStatus.Experimental
    Argument<T> withoutProfile(ArgumentProfileNamespace<?> profile);

    static <T> Argument<T> of(String name, Class<T> type) {
        return new SimpleArgument<>(name, TypeToken.of(type));
    }

    @Deprecated
    static <T> Argument<T> of(String name, Class<T> type, boolean nullable) {
        return new SimpleArgument<>(name, TypeToken.of(type), nullable);
    }

    static <T> Argument<T> of(String name, TypeToken<T> type) {
        return new SimpleArgument<>(name, type);
    }

    @Deprecated
    static <T> Argument<T> of(String name, TypeToken<T> type, boolean nullable) {
        return new SimpleArgument<>(name, type, nullable);
    }

    @ApiStatus.Experimental
    static <T, P extends ArgumentProfile<P>> Argument<T> profiled(String name, Class<T> type, P profile) {
        return profiled(name, TypeToken.of(type), profile);
    }

    @ApiStatus.Experimental
    static <T, P extends ArgumentProfile<P>> Argument<T> profiled(String name, TypeToken<T> type, P profile) {
        return new SimpleArgument<>(name, type)
            .addProfile(profile);
    }

}