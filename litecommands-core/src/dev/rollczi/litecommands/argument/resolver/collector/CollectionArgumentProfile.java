package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileKey;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;
import dev.rollczi.litecommands.reflect.type.TypeToken;

public class CollectionArgumentProfile implements ArgumentProfile<CollectionArgumentProfile> {

    private static final MetaKey<CollectionArgumentProfile> META_KEY = MetaKey.of("collector-meta", MetaType.of(CollectionArgumentProfile.class));
    public static final ArgumentProfileKey<CollectionArgumentProfile> KEY = ArgumentProfileKey.of(META_KEY);

    private final TypeToken<?> elementType;
    private final String delimiter;

    public CollectionArgumentProfile(TypeToken<?> elementType, String delimiter) {
        this.elementType = elementType;
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public TypeToken<?> getElementTypeToken() {
        return elementType;
    }

    @Override
    public ArgumentProfileKey<CollectionArgumentProfile> getKey() {
        return KEY;
    }

}
