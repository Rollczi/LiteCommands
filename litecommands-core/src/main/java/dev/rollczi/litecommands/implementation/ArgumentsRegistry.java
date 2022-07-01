package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.shared.StringUtils;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ArgumentsRegistry<SENDER> {

    private final Map<Class<? extends Annotation>, Map<Class<?>, Map<String, Argument<SENDER, ?>>>> arguments = new HashMap<>();

    public void register(Class<? extends Annotation> by, Class<?> on, String name, Argument<SENDER, ?> argument) {
        Map<Class<?>, Map<String, Argument<SENDER, ?>>> argumentsByClassesByName = arguments.computeIfAbsent(by, key -> new HashMap<>());
        Map<String, Argument<SENDER, ?>> argumentsByName = argumentsByClassesByName.computeIfAbsent(on, key -> new HashMap<>());

        argumentsByName.put(name, argument);
    }

    public void register(Class<? extends Annotation> by, Class<?> on, Argument<SENDER, ?> argument) {
        this.register(by, on, StringUtils.EMPTY, argument);
    }

    public Option<Argument<SENDER, ?>> getArgument(Class<? extends Annotation> by, Parameter on, String name) {
        Map<Class<?>, Map<String, Argument<SENDER, ?>>> byClasses = arguments.get(by);

        if (byClasses == null) {
            return Option.none();
        }

        Argument<SENDER, ?> argument = null;
        Argument<SENDER, ?> assignableArgument = null;

        for (Map.Entry<Class<?>, Map<String, Argument<SENDER, ?>>> entry : byClasses.entrySet()) {
            Map<String, Argument<SENDER, ?>> byNames = entry.getValue();
            Argument<SENDER, ?> argumentFromMap = byNames.get(name);

            if (argumentFromMap == null) {
                continue;
            }

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

    public Option<Argument<SENDER, ?>> getArgument(Class<? extends Annotation> by, Parameter on) {
        return this.getArgument(by, on, StringUtils.EMPTY);
    }

}
