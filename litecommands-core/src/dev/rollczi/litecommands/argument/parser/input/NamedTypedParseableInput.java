package dev.rollczi.litecommands.argument.parser.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;

import dev.rollczi.litecommands.priority.PriorityLevel;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import dev.rollczi.litecommands.shared.FailedReason;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

class NamedTypedParseableInput implements ParseableInput<NamedTypedParseableInput.TypeMixedInputMatcher> {

    private final List<String> routes = new ArrayList<>();
    private final Map<String, Object> namedArguments = new LinkedHashMap<>();

    NamedTypedParseableInput(List<String> routes, Map<String, Object> namedArguments) {
        this.routes.addAll(routes);
        this.namedArguments.putAll(namedArguments);
    }

    @Override
    public TypeMixedInputMatcher createMatcher() {
        return new TypeMixedInputMatcher();
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

    class TypeMixedInputMatcher implements ParseableInputMatcher<TypeMixedInputMatcher> {

        private int routePosition = 0;
        private final Set<String> consumedArguments = new HashSet<>();

        public TypeMixedInputMatcher() {}

        public TypeMixedInputMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <SENDER, T> ParseResult<T> nextArgument(
            Invocation<SENDER> invocation,
            Argument<T> argument,
            Supplier<Parser<SENDER, T>> parserProvider
        ) {
            Object input = namedArguments.get(argument.getName());

            if (input == null) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            Class<T> type = argument.getType().getRawType();
            consumedArguments.add(argument.getName());

            if (ReflectUtil.instanceOf(input, type)) {
                return ParseResult.success((T) input);
            }

            Parser<SENDER, T> parser = parserProvider.get();
            return parser.parse(invocation, argument, RawInput.of(input.toString().split(RawCommand.COMMAND_SEPARATOR)));
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
        public TypeMixedInputMatcher copy() {
            return new TypeMixedInputMatcher(routePosition);
        }

        @Override
        public EndResult endMatch(boolean isStrict) {
            if (consumedArguments.size() < namedArguments.size() && isStrict) {
                return EndResult.failed(FailedReason.of(InvalidUsage.Cause.TOO_MANY_ARGUMENTS, PriorityLevel.LOW));
            }

            return EndResult.success();
        }

    }

}
