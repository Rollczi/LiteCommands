package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.argument.ArgumentHandler;

public class ParameterHandler<T> {

    private final String name;
    private final ArgumentHandler<T> argumentHandler;

    public ParameterHandler(String name, ArgumentHandler<T> argumentHandler) {
        this.name = name;
        this.argumentHandler = argumentHandler;
    }

    public String getName() {
        return name;
    }

    public ArgumentHandler<T> getArgumentHandler() {
        return argumentHandler;
    }
}
