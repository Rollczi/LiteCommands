package dev.rollczi.litecommands.argument.parser.input;

import dev.rollczi.litecommands.input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ParseableInput<MATCHER extends ParsableInputMatcher<MATCHER>> extends Input<MATCHER> {

    static ParseableInput.NamedBuilder namedBuilder() {
        return new NamedBuilder();
    }

    static ParseableInput.RawBuilder rawBuilder() {
        return new RawBuilder();
    }

    static ParseableInput<?> raw(String... rawArguments) {
        return new RawParseableInput(Arrays.asList(rawArguments));
    }

    static ParseableInput<?> raw(List<String> rawArguments) {
        return new RawParseableInput(rawArguments);
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

        public ParseableInput<?> build() {
            if (isTyped) {
                return new NamedTypedParseableInput(routes, typedNamedArguments);
            }

            return new NamedParseableInput(routes, namedArguments);
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

        public ParseableInput<?> build() {
            return new RawParseableInput(routes);
        }
    }

}
