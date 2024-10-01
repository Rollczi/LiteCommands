package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileKey;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;

import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;

public interface Argument<T> extends Requirement<T> {

    String getName();

    ArgumentKey getKey();

    Optional<ParseResult<T>> defaultValue();

    boolean hasDefaultValue();

    @ApiStatus.Experimental
    <NEW> Argument<NEW> withType(TypeToken<NEW> type);

    static <T> Argument<T> of(String name, Class<T> type) {
        return new SimpleArgument<>(name, TypeToken.of(type), false);
    }

    static <T> Argument<T> of(String name, Class<T> type, boolean nullable) {
        return new SimpleArgument<>(name, TypeToken.of(type), nullable);
    }

    static <T> Argument<T> of(String name, TypeToken<T> type) {
        return new SimpleArgument<>(name, type, false);
    }

    static <T> Argument<T> of(String name, TypeToken<T> type, boolean nullable) {
        return new SimpleArgument<>(name, type, nullable);
    }

    @ApiStatus.Experimental
    static <T, P extends ArgumentProfile<P>> Argument<T> profiled(String name, Class<T> type, P profile) {
        return profiled(name, TypeToken.of(type), profile.getKey(), profile);
    }

    @ApiStatus.Experimental
    static <T, P extends ArgumentProfile<P>> Argument<T> profiled(String name, TypeToken<T> type, P profile) {
        return profiled(name, type, profile.getKey(), profile);
    }

    @ApiStatus.Experimental
    static <T, P> Argument<T> profiled(String name, Class<T> type, ArgumentProfileKey<P> key, P profile) {
        return profiled(name, TypeToken.of(type), key, profile);
    }

    @ApiStatus.Experimental
    static <T, P> Argument<T> profiled(String name, TypeToken<T> type, ArgumentProfileKey<P> key, P profile) {
        return  new SimpleArgument<>(name, type, false)
            .profiled(key, profile);
    }

}