package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileKey;
import dev.rollczi.litecommands.meta.MetaKey;

public class JoinProfile implements ArgumentProfile<JoinProfile> {

    public static final ArgumentProfileKey<JoinProfile> KEY = ArgumentProfileKey.of(MetaKey.of("join-argument", JoinProfile.class));

    public static final String DEFAULT_SEPARATOR = " ";
    public static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private final String separator;
    private final int limit;

    public JoinProfile(String separator, int limit) {
        this.separator = separator;
        this.limit = limit;
    }

    public JoinProfile() {
        this(DEFAULT_SEPARATOR, DEFAULT_LIMIT);
    }

    public String getSeparator() {
        return separator;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public ArgumentProfileKey<JoinProfile> getKey() {
        return KEY;
    }

}
