package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;
import dev.rollczi.litecommands.reflect.type.TypeToken;

public class VarargsProfile implements ArgumentProfile<VarargsProfile> {

    private static final MetaKey<VarargsProfile> META_KEY = MetaKey.of("profile:collector", MetaType.of(VarargsProfile.class));
    public static final ArgumentProfileNamespace<VarargsProfile> NAMESPACE = ArgumentProfileNamespace.of(META_KEY);

    private final TypeToken<?> elementType;
    private final String delimiter;

    public VarargsProfile(TypeToken<?> elementType, String delimiter) {
        this.elementType = elementType;
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public TypeToken<?> getElementType() {
        return elementType;
    }

    @Override
    public ArgumentProfileNamespace<VarargsProfile> getNamespace() {
        return NAMESPACE;
    }

}
