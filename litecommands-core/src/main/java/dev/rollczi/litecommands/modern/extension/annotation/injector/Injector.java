package dev.rollczi.litecommands.modern.extension.annotation.injector;

import dev.rollczi.litecommands.command.Invocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Injector<SENDER> {

    private final Map<Class<?>, Supplier<?>> instanceBindings = new HashMap<>();
    private final Map<Class<?>, Function<Invocation<SENDER>, ?>> contextualBindings = new HashMap<>();

}
