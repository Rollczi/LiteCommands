package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.priority.MutablePrioritizedList;
import dev.rollczi.litecommands.priority.PrioritizedList;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class SimpleArgument<T> implements MutableArgument<T> {

    private final String name;
    private final TypeToken<T> type;
    private final Meta meta = Meta.create();

    private ArgumentKey key;
    private volatile int hashCode;

    private @Nullable Argument<?> pattern;

    private final MutablePrioritizedList<ArgumentProfile<?>> profiles = new MutablePrioritizedList<>();

    @Deprecated
    private final boolean nullable;

    protected SimpleArgument(String name, TypeToken<T> type, @Nullable Argument<?> pattern) {
        this(name, type);
        this.pattern = pattern;
    }

    public SimpleArgument(String name, TypeToken<T> type) {
        this(name, type, false);
    }

    public SimpleArgument(String name, TypeToken<T> type, @Deprecated boolean nullable) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.setKey(ArgumentKey.of(this.getClass().getName(), name));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArgumentKey getKey() {
        return key;
    }

    @Override
    public void setKey(ArgumentKey key) {
        this.key = key;
        this.hashCode = createHashCode();
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
    public <P extends ArgumentProfile<P>> SimpleArgument<T> addProfile(P profile) {
        ArgumentProfileNamespace<P> namespace = profile.getNamespace();

        this.profiles.add(profile);
        this.meta.put(namespace.asMetaKey(), profile);

        if (this.profiles.first().equals(profile)) {
            this.setKey(this.key.withNamespace(namespace.getNamespace()));
        }
        return this;
    }

    @ApiStatus.Experimental
    @Override
    public <P> Optional<P> getProfile(ArgumentProfileNamespace<P> key) {
        return Optional.ofNullable(meta.get(key.asMetaKey(), null));
    }

    @Override
    public PrioritizedList<ArgumentProfile<?>> getProfiles() {
        return profiles;
    }

    @Override
    public <NEW> Argument<NEW> child(TypeToken<NEW> type) {
        return new SimpleArgument<>(name, type, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleArgument<?> that = (SimpleArgument<?>) o;
        return Objects.equals(getKey(), that.getKey()) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private int createHashCode() {
        return Objects.hash(getKey(), type);
    }

}
