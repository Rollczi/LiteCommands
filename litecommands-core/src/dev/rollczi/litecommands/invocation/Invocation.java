package dev.rollczi.litecommands.invocation;

import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.platform.PlatformSender;

public class Invocation<SENDER> {

    private final SENDER handle;
    private final PlatformSender platformSender;
    private final String command;
    private final String label;
    private final InputArguments<?> arguments;

    public Invocation(SENDER handle, PlatformSender platformSender, String command, String label, InputArguments<?> arguments) {
        this.handle = handle;
        this.platformSender = platformSender;
        this.command = command;
        this.label = label;
        this.arguments = arguments;
    }

    public SENDER getSender() {
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

    public InputArguments<?> arguments() {
        return this.arguments;
    }

}
