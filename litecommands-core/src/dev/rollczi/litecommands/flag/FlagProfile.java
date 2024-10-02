package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;

public class FlagProfile implements ArgumentProfile<FlagProfile> {

    public static final ArgumentProfileNamespace<FlagProfile> NAMESPACE = ArgumentProfileNamespace.of(MetaKey.of("profile:flag", MetaType.of(FlagProfile.class)));

    private final String value;

    public FlagProfile(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public ArgumentProfileNamespace<FlagProfile> getNamespace() {
        return NAMESPACE;
    }

}
