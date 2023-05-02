package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.parser.ArgumentParseException;
import dev.rollczi.litecommands.argument.parser.ArgumentParser;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class NamedInputArguments implements InputArguments<NamedInputArguments.NamedMatcher> {

    private final List<String> routes = new ArrayList<>();
    private final Map<String, String> namedArguments = new LinkedHashMap<>();

    NamedInputArguments(List<String> routes, Map<String, String> namedArguments) {
        this.routes.addAll(routes);
        this.namedArguments.putAll(namedArguments);
    }

    @Override
    public NamedMatcher createMatcher() {
        return new NamedMatcher();
    }

    @Override
    public List<String> asList() {
        List<String> rawArgs = new ArrayList<>();

        namedArguments.forEach((name, value) -> {
            rawArgs.add(name);
            rawArgs.add(value);
        });

        return Collections.unmodifiableList(rawArgs);
    }

    public class NamedMatcher implements InputArgumentsMatcher<NamedMatcher> {

        private int routePosition = 0;
        private Set<String> consumedArguments = new HashSet<>();

        public NamedMatcher() {}

        public NamedMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        public <SENDER, PARSED> ArgumentResult<PARSED> matchArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ArgumentParserSet<SENDER, PARSED> parserSet) {
            String input = namedArguments.get(argument.getName());

            if (input == null) {
                return ArgumentResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            consumedArguments.add(argument.getName());
            return this.parseInput(invocation, argument, parserSet, input);
        }

        @SuppressWarnings("unchecked")
        private <SENDER, INPUT, PARSED> ArgumentResult<PARSED> parseInput(Invocation<SENDER> invocation, Argument<PARSED> argument, ArgumentParserSet<SENDER, PARSED> parserSet, INPUT input) {
            Class<INPUT> inputType = (Class<INPUT>) input.getClass();
            ArgumentParser<SENDER, INPUT, PARSED> parser = parserSet.getParser(inputType)
                .orElseThrow(() -> new ArgumentParseException("No parser for input type " + inputType.getName()));

            return parser.parse(invocation, argument, input);
        }

        @Override
        public boolean hasNextRoute() {
            return routePosition < routes.size();
        }

        @Override
        public String showNextRoute() {
            return routes.get(routePosition);
        }

        @Override
        public String matchNextRoute() {
            return routes.get(routePosition++);
        }

        @Override
        public NamedMatcher copy() {
            return new NamedMatcher(this.routePosition);
        }

        @Override
        public EndResult endMatch() {
            if (consumedArguments.size() != namedArguments.size()) {
                return EndResult.failed(InvalidUsage.Cause.TOO_FEW_ARGUMENTS);
            }

            return EndResult.success();
        }

    }

}
