package dev.rollczi.litecommands.argument.parser.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class NamedParseableInput implements ParseableInput<NamedParseableInput.NamedParseableInputMatcher> {

    private final List<String> routes = new ArrayList<>();
    private final Map<String, String> namedArguments = new LinkedHashMap<>();

    NamedParseableInput(List<String> routes, Map<String, String> namedArguments) {
        this.routes.addAll(routes);
        this.namedArguments.putAll(namedArguments);
    }

    @Override
    public NamedParseableInputMatcher createMatcher() {
        return new NamedParseableInputMatcher();
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

    public class NamedParseableInputMatcher implements ParseableInputMatcher<NamedParseableInputMatcher> {

        private final List<String> consumedArguments = new ArrayList<>();

        private int routePosition = 0;

        public NamedParseableInputMatcher() {}

        public NamedParseableInputMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        public <SENDER, PARSED> ParseResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, Parser<SENDER, PARSED> parser) {
            String input = namedArguments.get(argument.getName());

            if (input == null) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            consumedArguments.add(argument.getName());

            return parser.parse(invocation, argument, RawInput.of(input.split(RawCommand.COMMAND_SEPARATOR)));
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
        public NamedParseableInputMatcher copy() {
            return new NamedParseableInputMatcher(this.routePosition);
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
