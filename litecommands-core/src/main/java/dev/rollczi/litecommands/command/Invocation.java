package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.platform.LiteSender;

import java.util.Optional;

public class Invocation<SENDER> {

    private final SENDER handle;
    private final LiteSender sender;
    private final String command;
    private final String label;
    private final String[] args;

    public Invocation(SENDER handle, LiteSender sender, String command, String label, String[] args) {
        this.handle = handle;
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public SENDER handle() {
        return handle;
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

    public LiteInvocation toLite() {
        return new LiteInvocation(this.sender, command, label, args);
    }
}
