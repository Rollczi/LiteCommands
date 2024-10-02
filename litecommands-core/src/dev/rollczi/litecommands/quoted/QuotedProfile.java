package dev.rollczi.litecommands.quoted;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;

public class QuotedProfile implements ArgumentProfile<QuotedProfile> {

    public static final ArgumentProfileNamespace<QuotedProfile> NAMESPACE = ArgumentProfileNamespace.of(MetaKey.of("profile:quoted", MetaType.of(QuotedProfile.class)));

    @Override
    public ArgumentProfileNamespace<QuotedProfile> getNamespace() {
        return NAMESPACE;
    }

}
