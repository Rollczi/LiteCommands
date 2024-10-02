package dev.rollczi.litecommands.argument.resolver.nullable;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.meta.MetaKey;

public class NullableProfile implements ArgumentProfile<NullableProfile> {

    public static final ArgumentProfileNamespace<NullableProfile> NAMESPACE = ArgumentProfileNamespace.of(MetaKey.of("profile:nullable", NullableProfile.class));

    @Override
    public ArgumentProfileNamespace<NullableProfile> getNamespace() {
        return NAMESPACE;
    }

}
