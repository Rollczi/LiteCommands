package dev.rollczi.litecommands.argument.profile;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.meta.MetaKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Experimental
public class ArgumentProfileNamespace<META_HOLDER> implements ArgumentKey {

    private final String argumentNamespace;
    private final String argumentKey;
    private final MetaKey<META_HOLDER> metaKey;

    protected ArgumentProfileNamespace(MetaKey<META_HOLDER> metaKey, String argumentKey) {
        this.argumentNamespace = metaKey.getKey();
        this.argumentKey = argumentKey;
        this.metaKey = metaKey;
    }

    @Override
    public String getKey() {
        return argumentKey;
    }

    @ApiStatus.Experimental
    public MetaKey<META_HOLDER> asMetaKey() {
        return metaKey;
    }

    @Override
    public String getNamespace() {
        return argumentNamespace;
    }

    @Override
    public ArgumentKey withKey(String argumentKey) {
        return new ArgumentProfileNamespace<>(this.metaKey, argumentKey);
    }

    @Override
    public ArgumentKey withNamespace(String namespace) {
        return ArgumentKey.of(namespace, getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArgumentProfileNamespace<?> that = (ArgumentProfileNamespace<?>) o;
        return Objects.equals(argumentNamespace, that.argumentNamespace) && Objects.equals(argumentKey, that.argumentKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(argumentNamespace, argumentKey);
    }

    @ApiStatus.Experimental
    public static <T> ArgumentProfileNamespace<T> of(MetaKey<T> metaKey) {
        return of(metaKey, ArgumentKey.DEFAULT_KEY);
    }

    @ApiStatus.Experimental
    public static <T> ArgumentProfileNamespace<T> of(String namespace, Class<T> type) {
        return of(MetaKey.of(namespace, type));
    }

    @ApiStatus.Experimental
    public static <T> ArgumentProfileNamespace<T> of(MetaKey<T> metaKey, String argumentKey) {
        return new ArgumentProfileNamespace<>(metaKey, argumentKey);
    }

}
