package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.command.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ArgumentsInput<MATCHER extends ArgumentsInputMatcher<MATCHER>> extends Input<MATCHER> {

    static ArgumentsInput.NamedBuilder namedBuilder() {
        return new NamedBuilder();
    }

    static ArgumentsInput.RawBuilder rawBuilder() {
        return new RawBuilder();
    }

    static ArgumentsInput<?> raw(String... rawArguments) {
        return new RawArgumentsInput(Arrays.asList(rawArguments));
    }

    static ArgumentsInput<?> raw(List<String> rawArguments) {
        return new RawArgumentsInput(rawArguments);
    }

    class NamedBuilder {
        private final List<String> routes = new ArrayList<>();
        private final Map<String, String> namedArguments = new LinkedHashMap<>();
        private final Map<String, Object> typedNamedArguments = new LinkedHashMap<>();
        private boolean isTyped = false;

        public NamedBuilder literal(String route) {
            routes.add(route);
            return this;
        }

        public NamedBuilder namedArgument(String name, String value) {
            namedArguments.put(name, value);
            typedNamedArguments.put(name, value);
            return this;
        }

        public <T> NamedBuilder namedTypedArgument(String name, T value) {
            typedNamedArguments.put(name, value);
            isTyped = true;
            return this;
        }

        public ArgumentsInput<?> build() {
            if (isTyped) {
                return new NamedTypedArgumentsInput(routes, typedNamedArguments);
            }

            return new NamedArgumentsInput(routes, namedArguments);
        }
    }

    class RawBuilder {
        private final List<String> routes = new ArrayList<>();

        public RawBuilder literal(String route) {
            routes.add(route);
            return this;
        }

        public RawBuilder rawArgument(String value) {
            routes.add(value);
            return this;
        }

        public ArgumentsInput<?> build() {
            return new RawArgumentsInput(routes);
        }
    }

}
