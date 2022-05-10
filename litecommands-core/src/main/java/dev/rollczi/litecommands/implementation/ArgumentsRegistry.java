package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.Argument;
import panda.std.Option;
import panda.utilities.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

class ArgumentsRegistry {

    private final Map<Class<? extends Annotation>, Map<Class<?>, Map<String, Argument<?>>>> arguments = new HashMap<>();

    public void register(Class<? extends Annotation> by, Class<?> on, String name, Argument<?> argument) {
        Map<Class<?>, Map<String, Argument<?>>> argumentsByClassesByName = arguments.computeIfAbsent(by, key -> new HashMap<>());
        Map<String, Argument<?>> argumentsByName = argumentsByClassesByName.computeIfAbsent(on, key -> new HashMap<>());

        argumentsByName.put(name, argument);
    }

    public void register(Class<? extends Annotation> by, Class<?> on, Argument<?> argument) {
        this.register(by, on, StringUtils.EMPTY, argument);
    }

    public Option<Argument<?>> getArgument(Class<? extends Annotation> by, Parameter on, String name) {
        Map<Class<?>, Map<String, Argument<?>>> byClasses = arguments.get(by);

        if (byClasses == null) {
            return Option.none();
        }

        Argument<?> argument = null;
        Argument<?> assignableArgument = null;

        for (Map.Entry<Class<?>, Map<String, Argument<?>>> entry : byClasses.entrySet()) {
            Map<String, Argument<?>> byNames = entry.getValue();
            Argument<?> argumentFromMap = byNames.get(name);
            Class<?> type = entry.getKey();

            if (argumentFromMap.canHandle(type, on)) {
                argument = argumentFromMap;
                break;
            }

            if (argumentFromMap.canHandleAssignableFrom(type, on)) {
                assignableArgument = argumentFromMap;
            }
        }


        return Option.of(argument == null ? assignableArgument : argument);
    }

    public Option<Argument<?>> getArgument(Class<? extends Annotation> by, Parameter on) {
        return this.getArgument(by, on, StringUtils.EMPTY);
    }

}
