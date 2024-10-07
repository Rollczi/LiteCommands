package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.priority.MutablePrioritizedList;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class SimpleArgument<T> implements Argument<T> {

    private final String name;
    private final TypeToken<T> type;
    private final Meta meta = Meta.create();
    private @Nullable Argument<?> pattern;
    @Deprecated
    private final boolean nullable;
    private final MutablePrioritizedList<ArgumentProfile<?>> profiles = new MutablePrioritizedList<>();

    public SimpleArgument(String name, TypeToken<T> type, @Deprecated boolean nullable) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.meta.put(Meta.ARGUMENT_KEY, ArgumentKey.of(this.getClass().getName(), name));
    }

    public SimpleArgument(String name, TypeToken<T> type) {
        this.name = name;
        this.type = type;
        this.nullable = false;
        this.meta.put(Meta.ARGUMENT_KEY, ArgumentKey.of(this.getClass().getName(), name));
    }

    protected SimpleArgument(String name, TypeToken<T> type, Argument<?> pattern) {
        this.name = name;
        this.type = type;
        this.nullable = false;
        this.pattern = pattern;
        this.meta.put(Meta.ARGUMENT_KEY, ArgumentKey.of(this.getClass().getName(), name));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArgumentKey getKey() {
        return meta.get(Meta.ARGUMENT_KEY);
    }

    @Override
    public TypeToken<T> getType() {
        return type;
    }

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return pattern;
    }

    @Override
    public Optional<ParseResult<T>> getDefaultValue() {
        if (nullable) {
            return Optional.of(ParseResult.successNull());
        }

        return Optional.empty();
    }

    @Override
    public boolean hasDefaultValue() {
        return nullable || getDefaultValue().isPresent();
    }

    @ApiStatus.Experimental
    public <P extends ArgumentProfile<P>> SimpleArgument<T> withProfile(P profile) {
        ArgumentProfileNamespace<P> namespace = profile.getNamespace();

        this.profiles.add(profile);
        this.meta.put(namespace.asMetaKey(), profile);

        if (this.profiles.first().equals(profile)) {
            this.meta.edit(Meta.ARGUMENT_KEY, argumentKey -> argumentKey.withNamespace(namespace.getNamespace()));

        }
        return this;
    }

    @ApiStatus.Experimental
    @Override
    public <P> Optional<P> getProfile(ArgumentProfileNamespace<P> key) {
        return Optional.ofNullable(meta.get(key.asMetaKey(), null));
    }

    @Override
    public <NEW> Argument<NEW> createChild(TypeToken<NEW> type) {
        return new SimpleArgument<>(name, type, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleArgument<?> that = (SimpleArgument<?>) o;
        return Objects.equals(getKey(), that.getKey()) && Objects.equals(type, that.type);
    }

    private volatile int hashCode;
    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(getKey(), type);
            hashCode = result;
        }
        return result;
    }

}
