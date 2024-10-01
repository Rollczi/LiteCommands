package dev.rollczi.litecommands.argument.profile;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.meta.MetaKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ArgumentProfileKey<META_HOLDER> implements ArgumentKey {

    private final String argumentKey;
    private final MetaKey<META_HOLDER> metaKey;

    protected ArgumentProfileKey(MetaKey<META_HOLDER> metaKey, String argumentKey) {
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
        return metaKey.getKey();
    }

    @Override
    public ArgumentKey withKey(String argumentKey) {
        return new ArgumentProfileKey<>(this.metaKey, argumentKey);
    }

    @Override
    public ArgumentKey withNamespace(String namespace) {
        return ArgumentKey.of(namespace, getKey());
    }

    @ApiStatus.Experimental
    public static <T> ArgumentProfileKey<T> of(MetaKey<T> metaKey) {
        return of(metaKey, ArgumentKey.DEFAULT_KEY);
    }

    @ApiStatus.Experimental
    public static <T> ArgumentProfileKey<T> of(MetaKey<T> metaKey, String argumentKey) {
        return new ArgumentProfileKey<>(metaKey, argumentKey);
    }

}
