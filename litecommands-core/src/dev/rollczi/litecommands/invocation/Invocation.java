package dev.rollczi.litecommands.invocation;

import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.platform.PlatformSender;

public class Invocation<SENDER> {

    private final PlatformSender platformSender;
    private final String command;
    private final String label;
    private final Input<?> arguments;
    private final InvocationContext context;

    public Invocation(PlatformSender platformSender, String command, String label, Input<?> input, InvocationContext context) {
        this.platformSender = platformSender;
        this.command = command;
        this.label = label;
        this.arguments = input;
        this.context = context;
    }

    public Invocation(PlatformSender platformSender, String command, String label, Input<?> input) {
        this.platformSender = platformSender;
        this.command = command;
        this.label = label;
        this.arguments = input;
        this.context = InvocationContext.builder().build();
    }

    public SENDER sender() {
        return (SENDER) this.platformSender.getHandle();
    }

    public PlatformSender platformSender() {
        return this.platformSender;
    }

    public String name() {
        return this.command;
    }

    public String label() {
        return this.label;
    }

    public Input<?> arguments() {
        return this.arguments;
    }

    public InvocationContext context() {
        return context;
    }

    @Override
    public String toString() {
        return "Invocation{'" + platformSender.getName() + "'" +
            " executed '/" + command + " " + String.join(" ", arguments.asList()) + "'}";
    }
}
