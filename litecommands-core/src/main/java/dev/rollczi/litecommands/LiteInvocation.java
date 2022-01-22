package dev.rollczi.litecommands;

import dev.rollczi.litecommands.platform.LiteSender;

public class LiteInvocation {

    private final String name;
    private final String alias;
    private final LiteSender sender;
    private final String[] arguments;

    public LiteInvocation(String name, String alias, LiteSender sender, String[] arguments) {
        this.name = name;
        this.alias = alias;
        this.sender = sender;
        this.arguments = arguments;
    }

    public LiteInvocation(String name, LiteSender sender, String[] arguments) {
        this(name, name, sender, arguments);
    }

    public String name() {
        return name;
    }

    public String alias() {
        return alias;
    }

    public LiteSender sender() {
        return sender;
    }

    public String[] arguments() {
        return arguments;
    }

}
