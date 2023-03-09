package dev.rollczi.litecommands.modern.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiteAnnotatedCommands {

    private final List<Object> commandsInstances = new ArrayList<>();
    private final List<Class<?>> commandsClasses = new ArrayList<>();

    public LiteAnnotatedCommands command(Object... commands) {
        for (Object command : commands) {
            if (command == null) {
                throw new IllegalArgumentException("Command cannot be null");
            }

            if (command instanceof Class<?>) {
                commandsClasses.add((Class<?>) command);
                continue;
            }

            commandsInstances.add(command);
        }

        return this;
    }

    public LiteAnnotatedCommands command(Class<?>... commands) {
        for (Class<?> command : commands) {
            if (command == null) {
                throw new IllegalArgumentException("Command cannot be null");
            }

            commandsClasses.add(command);
        }

        return this;
    }

    List<Object> getCommandsInstances() {
        return Collections.unmodifiableList(commandsInstances);
    }

    List<Class<?>> getCommandsClasses() {
        return Collections.unmodifiableList(commandsClasses);
    }

    public <SENDER> LiteAnnotationExtension<SENDER> build() {
        return new LiteAnnotationExtension<>(this);
    }

}
