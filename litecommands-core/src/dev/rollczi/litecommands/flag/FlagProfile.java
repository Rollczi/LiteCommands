package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileKey;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;

public class FlagProfile implements ArgumentProfile<FlagProfile> {

    public static final ArgumentProfileKey<FlagProfile> KEY = ArgumentProfileKey.of(MetaKey.of("flag-meta", MetaType.of(FlagProfile.class)));

    private final String value;

    public FlagProfile(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public ArgumentProfileKey<FlagProfile> getKey() {
        return KEY;
    }

}
