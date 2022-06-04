package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.platform.LiteSender;

import java.util.Optional;

public class LiteInvocation {

    private final LiteSender sender;
    private final String command;
    private final String label;
    private final String[] args;

    public LiteInvocation(LiteSender sender, String command, String label, String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public LiteSender sender() {
        return sender;
    }

    public String name() {
        return command;
    }

    public String label() {
        return label;
    }

    public String[] arguments() {
        return args;
    }

    public Optional<String> argument(int route) {
        return route < args.length && route >= 0 ? Optional.of(args[route]) : Optional.empty();
    }

    public Optional<String> lastArgument() {
        return this.argument(this.arguments().length - 1);
    }

}
