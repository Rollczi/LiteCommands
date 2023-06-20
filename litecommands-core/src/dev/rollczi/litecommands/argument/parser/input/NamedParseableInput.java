package dev.rollczi.litecommands.argument.parser.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.LiteParseException;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class NamedParseableInput implements ParseableInput<NamedParseableInput.NamedInputMatcher> {

    private final List<String> routes = new ArrayList<>();
    private final Map<String, String> namedArguments = new LinkedHashMap<>();

    NamedParseableInput(List<String> routes, Map<String, String> namedArguments) {
        this.routes.addAll(routes);
        this.namedArguments.putAll(namedArguments);
    }

    @Override
    public NamedInputMatcher createMatcher() {
        return new NamedInputMatcher();
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

    public class NamedInputMatcher implements ParsableInputMatcher<NamedInputMatcher> {

        private final List<String> consumedArguments = new ArrayList<>();

        private int routePosition = 0;

        public NamedInputMatcher() {}

        public NamedInputMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        public <SENDER, PARSED> ParseResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ParserSet<SENDER, PARSED> parserSet) {
            String input = namedArguments.get(argument.getName());

            if (input == null) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            consumedArguments.add(argument.getName());
            return this.parseInput(invocation, argument, parserSet, input);
        }

        @SuppressWarnings("unchecked")
        private <SENDER, INPUT, PARSED> ParseResult<PARSED> parseInput(Invocation<SENDER> invocation, Argument<PARSED> argument, ParserSet<SENDER, PARSED> parserSet, INPUT input) {
            Class<INPUT> inputType = (Class<INPUT>) input.getClass();
            Parser<SENDER, INPUT, PARSED> parser = parserSet.getParser(inputType)
                .orElseThrow(() -> new LiteParseException("No parser for input type " + inputType.getName()));

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
        public String nextRoute() {
            return routes.get(routePosition++);
        }

        @Override
        public NamedInputMatcher copy() {
            return new NamedInputMatcher(this.routePosition);
        }

        @Override
        public EndResult endMatch() {
            if (consumedArguments.size() < namedArguments.size()) {
                return EndResult.failed(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
            }

            return EndResult.success();
        }

    }

}
