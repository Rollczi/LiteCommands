package dev.rollczi.litecommands.modern.invocation;

import dev.rollczi.litecommands.modern.platform.PlatformSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Invocation<SENDER> {

    private final SENDER handle;
    private final PlatformSender platformSender;
    private final String command;
    private final String label;
    private final List<String> rawArguments;

    public Invocation(SENDER handle, PlatformSender platformSender, String command, String label, String[] rawArguments) {
        this.handle = handle;
        this.platformSender = platformSender;
        this.command = command;
        this.label = label;
        this.rawArguments = Arrays.asList(rawArguments);
    }

    public SENDER handle() {
        return this.handle;
    }

    public PlatformSender getPlatformSender() {
        return this.platformSender;
    }

    public String name() {
        return this.command;
    }

    public String label() {
        return this.label;
    }

    public String[] arguments() {
        return this.rawArguments.toArray(new String[0]);
    }

    public List<String> argumentsList() {
        return Collections.unmodifiableList(this.rawArguments);
    }

    public Optional<String> argument(int route) {
        return route < this.rawArguments.size() && route >= 0 ? Optional.of(this.rawArguments.get(route)) : Optional.empty();
    }

    public Optional<String> firstArgument() {
        return this.argument(0);
    }

    public Optional<String> lastArgument() {
        return this.argument(this.arguments().length - 1);
    }

}
