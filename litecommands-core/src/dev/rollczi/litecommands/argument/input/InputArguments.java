package dev.rollczi.litecommands.argument.input;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface InputArguments<MATCHER extends InputArgumentsMatcher<MATCHER>> {

    MATCHER createMatcher();

    String[] asArray();

    List<String> asList();

    static InputArguments.NamedBuilder namedBuilder() {
        return new NamedBuilder();
    }

    class NamedBuilder {
        private final List<String> routes = new ArrayList<>();
        private final Map<String, String> namedArguments = new LinkedHashMap<>();

        public NamedBuilder literal(String route) {
            routes.add(route);
            return this;
        }

        public NamedBuilder namedArgument(String name, String value) {
            namedArguments.put(name, value);
            return this;
        }

        public InputArguments<?> build() {
            return new NamedInputArguments(routes, namedArguments);
        }

    }
}
