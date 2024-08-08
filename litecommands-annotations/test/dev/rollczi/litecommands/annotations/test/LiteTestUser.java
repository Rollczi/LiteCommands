package dev.rollczi.litecommands.annotations.test;

import org.jetbrains.annotations.Nullable;

public class LiteTestUser {

    private final String name;
    private LiteTestGuild guild;

    public LiteTestUser(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public @Nullable LiteTestGuild getGuild() {
        return this.guild;
    }

    public void setGuild(String name) {
        this.guild = new LiteTestGuild(name);
    }

}
