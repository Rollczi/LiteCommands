package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.parser.ArgumentParseException;
import dev.rollczi.litecommands.argument.parser.ArgumentParser;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.util.Preconditions;

import java.util.List;

public class RawInputAnalyzer {

    private final List<String> rawArguments;
    private int pivotPosition = 0;

    public RawInputAnalyzer(List<String> rawArguments) {
        for (String rawArgument : rawArguments) {
            Preconditions.notNull(rawArgument, "raw argument");
        }

        this.rawArguments = rawArguments;
    }

    public int getPivotPosition() {
        return pivotPosition;
    }

    public void setPivotPosition(int pivotPosition) {
        this.pivotPosition = pivotPosition;
    }

    public boolean hasNextRoute() {
        return pivotPosition < rawArguments.size();
    }

    public <SENDER, T> Context<SENDER, T> toContext(
        Argument<T> argument,
        ArgumentParserSet<SENDER, T> parserSet
    ) {
        return new Context<>(argument, parserSet);
    }

    public String showNextRoute() {
        return rawArguments.get(pivotPosition);
    }

    public String nextRoute() {
        return rawArguments.get(pivotPosition++);
    }

    public String getLastArgument() {
        return rawArguments.get(rawArguments.size() - 1);
    }

    public boolean nextRouteIsLast() {
        return pivotPosition == rawArguments.size() - 1;
    }

    public class Context<SENDER, T> {

        private final ArgumentParser<SENDER, RawInput, T> parser;
        private final Argument<T> argument;
        private final int minArguments;
        private final int maxArguments;

        public Context(
            Argument<T> argument,
            ArgumentParserSet<SENDER, T> parserSet
        ) {
            this.argument = argument;
            this.parser = parserSet.getParser(RawInput.class)
                .orElseThrow(() -> new ArgumentParseException("No parser for RawInput -> " + argument.getWrapperFormat().getParsedType().getName()));

            Range range = parser.getRange();

            this.minArguments = range.getMin() + pivotPosition;
            this.maxArguments = calculateMaxArguments(rawArguments, range, pivotPosition);
        }

        public ArgumentResult<T> parseArgument(Invocation<SENDER> invocation) {
            List<String> arguments = rawArguments.subList(pivotPosition, maxArguments);
            RawInput input = RawInput.of(arguments);
            ArgumentResult<T> parsed = parser.parse(invocation, argument, input);

            pivotPosition += input.consumedCount();

            return parsed;
        }

        public boolean isLastRawArgument() {
            return pivotPosition == rawArguments.size() - 1 || minArguments == rawArguments.size();
        }

        public boolean isParsedLast() {
            return pivotPosition == rawArguments.size();
        }

        public boolean isMissingFullArgument() {
            return !hasNextRoute() && minArguments > 0;
        }

        public boolean isMissingPartOfArgument() {
            return minArguments > rawArguments.size();
        }

        public List<String> getAllNotConsumedArguments() {
            return rawArguments.subList(pivotPosition, rawArguments.size());
        }

    }

    private static int calculateMaxArguments(List<String> rawArguments, Range range, int routePosition) {
        int maxArguments = range.getMax() == Integer.MAX_VALUE
            ? rawArguments.size()
            : routePosition + range.getMax();

        return Math.min(maxArguments, rawArguments.size());
    }

}