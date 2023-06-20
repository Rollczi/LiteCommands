package dev.rollczi.litecommands.argument.parser.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.input.InputMatcher;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.FailedReason;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ParseableInput<MATCHER extends ParseableInput.ParsableInputMatcher<MATCHER>> extends Input<MATCHER> {

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

    interface ParsableInputMatcher<SELF extends ParsableInputMatcher<SELF>> extends InputMatcher {

        <SENDER, PARSED> ParseResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ParserSet<SENDER, PARSED> parserSet);

        @Override
        boolean hasNextRoute();

        @Override
        String nextRoute();

        @Override
        String showNextRoute();

        SELF copy();

        EndResult endMatch();

        class EndResult {
            private final boolean successful;
            private final FailedReason failedReason;

            private EndResult(boolean successful, FailedReason failedReason) {
                this.successful = successful;
                this.failedReason = failedReason;
            }

            public boolean isSuccessful() {
                return successful;
            }

            public FailedReason getFailedReason() {
                return failedReason;
            }

            public static EndResult success() {
                return new EndResult(true, null);
            }

            public static EndResult failed(FailedReason failedReason) {
                return new EndResult(true, failedReason);
            }

            public static EndResult failed(Object cause) {
                return new EndResult(false, FailedReason.of(cause));
            }
        }

    }
}
