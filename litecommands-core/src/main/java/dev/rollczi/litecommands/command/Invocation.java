package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.platform.LiteSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Invocation<SENDER> {

    private final SENDER handle;
    private final LiteSender sender;
    private final String command;
    private final String label;
    private final List<String> rawArguments;

    public Invocation(SENDER handle, LiteSender sender, String command, String label, String[] rawArguments) {
        this.handle = handle;
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.rawArguments = Arrays.asList(rawArguments);
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
        return rawArguments.toArray(new String[0]);
    }

    public List<String> argumentsList() {
        return Collections.unmodifiableList(rawArguments);
    }

    public Optional<String> argument(int route) {
        return route < rawArguments.size() && route >= 0 ? Optional.of(rawArguments.get(route)) : Optional.empty();
    }

    public Optional<String> firstArgument() {
        return this.argument(0);
    }

    public Optional<String> lastArgument() {
        return this.argument(this.arguments().length - 1);
    }



    @Deprecated
    public LiteInvocation toLite() {
        return new LiteInvocation(this.sender, command, label, this.arguments());
    }

}
