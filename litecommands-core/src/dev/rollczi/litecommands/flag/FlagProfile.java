package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaType;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class FlagProfile implements ArgumentProfile<FlagProfile> {

    public static final ArgumentProfileNamespace<FlagProfile> NAMESPACE = ArgumentProfileNamespace.of(MetaKey.of("profile:flag", MetaType.of(FlagProfile.class)));

    private final List<String> names;

    public FlagProfile(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public @NotNull ArgumentProfileNamespace<FlagProfile> getNamespace() {
        return NAMESPACE;
    }

}
