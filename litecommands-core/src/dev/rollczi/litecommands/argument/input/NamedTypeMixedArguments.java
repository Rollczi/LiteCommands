package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class NamedTypeMixedArguments implements InputArguments<NamedTypeMixedArguments.TypeMixedMatcher> {

    private final List<String> routes = new ArrayList<>();
    private final Map<String, Object> namedArguments = new HashMap<>();

    @Override
    public TypeMixedMatcher createMatcher() {
        return new TypeMixedMatcher();
    }

    @Override
    public String[] asArray() {
        return this.asList().toArray(new String[0]);
    }

    @Override
    public List<String> asList() {
        List<String> rawArgs = new ArrayList<>();

        namedArguments.forEach((named, object) -> {
            rawArgs.add(named);
            rawArgs.add(object.toString());
        });

        return Collections.unmodifiableList(rawArgs);
    }

    public class TypeMixedMatcher implements InputArgumentsMatcher<TypeMixedMatcher> {

        private int routePosition = 0;
        private Set<String> consumedArguments = new HashSet<>();

        public TypeMixedMatcher() {}

        public TypeMixedMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        public <SENDER, PARSED> ArgumentResult<PARSED> matchArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ArgumentParserSet<SENDER, PARSED> parserSet) {
            Object input = namedArguments.get(argument.getName());

            if (input == null) {
                return ArgumentResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            Class<PARSED> outType = argument.getWrapperFormat().getType();
            consumedArguments.add(argument.getName());

            if (outType.isAssignableFrom(input.getClass())) {
                return ArgumentResult.success((PARSED) input);
            }

            return this.parseInput(invocation, argument, parserSet, input);
        }

        @SuppressWarnings("unchecked")
        private <SENDER, INPUT, OUT> ArgumentResult<OUT> parseInput(Invocation<SENDER> invocation, Argument<OUT> argument, ArgumentParserSet<SENDER, OUT> parserSet, INPUT input) {
            Class<INPUT> inputType = (Class<INPUT>) input.getClass();
            ArgumentParser<SENDER, INPUT, OUT> parser = parserSet.getParser(inputType)
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
        public TypeMixedMatcher copy() {
            return new TypeMixedMatcher(routePosition);
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
