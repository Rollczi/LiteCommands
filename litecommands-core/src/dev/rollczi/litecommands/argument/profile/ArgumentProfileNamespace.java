package dev.rollczi.litecommands.argument.profile;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.meta.MetaKey;
import org.jetbrains.annotations.ApiStatus;

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

    @ApiStatus.Experimental
    public static <T> ArgumentProfileNamespace<T> of(MetaKey<T> metaKey) {
        return of(metaKey, ArgumentKey.DEFAULT_KEY);
    }

    @ApiStatus.Experimental
    public static <T> ArgumentProfileNamespace<T> of(MetaKey<T> metaKey, String argumentKey) {
        return new ArgumentProfileNamespace<>(metaKey, argumentKey);
    }

}
