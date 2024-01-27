package dev.rollczi.litecommands.input.raw;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.shared.Preconditions;

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
        Invocation<SENDER> invocation,
        Argument<T> argument,
        ParserSet<SENDER, T> parserSet
    ) {
        return new Context<>(invocation, argument, parserSet);
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

    public <SENDER, T> boolean isNextOptional(ParserSet<SENDER, T> parserSet, Invocation<SENDER> invocation, Argument<T> argument) {
        Parser<SENDER, T> validParser = parserSet.getValidParserOrThrow(null, argument);
        Range range = validParser.getRange(argument);

        return range.getMin() == 0;
    }

    public class Context<SENDER, T> {

        private final Parser<SENDER, T> parser;
        private final Argument<T> argument;
        private final int argumentMinCount;
        private final int argumentMaxCount;
        private final int realArgumentMaxCount;
        private final int realArgumentMinCount;

        public Context(
            Invocation<SENDER> invocation,
            Argument<T> argument,
            ParserSet<SENDER, T> parserSet
        ) {
            this.argument = argument;
            this.parser = parserSet.getValidParserOrThrow(invocation, argument);
            Range range = parser.getRange(argument);

            this.argumentMinCount = range.getMin() + pivotPosition;
            this.argumentMaxCount = calculateMaxCount(range);
            this.realArgumentMinCount = Math.min(argumentMinCount, rawArguments.size());
            this.realArgumentMaxCount = calculateRealMaxCount(rawArguments, range, pivotPosition);
        }

        public ParseResult<T> parseArgument(Invocation<SENDER> invocation) {
            List<String> arguments = rawArguments.subList(pivotPosition, realArgumentMaxCount);
            RawInput input = RawInput.of(arguments);
            ParseResult<T> parsed = parser.parse(invocation, argument, input);

            pivotPosition += input.consumedCount();

            return parsed;
        }

        public boolean isLastRawArgument() {
            return pivotPosition == rawArguments.size() - 1 || realArgumentMaxCount >= rawArguments.size();
        }

        public boolean isMissingFullArgument() {
            return isNoMoreArguments() && isMissingPartOfArgument();
        }

        public boolean isNoMoreArguments() {
            return pivotPosition >= rawArguments.size();
        }

        public boolean isMissingPartOfArgument() {
            return argumentMinCount != realArgumentMinCount;
        }

        public boolean isPotentialLastArgument() {
            return this.realArgumentMaxCount < this.argumentMaxCount;
        }

        public List<String> getAllNotConsumedArguments() {
            return rawArguments.subList(pivotPosition, rawArguments.size());
        }

    }

    private static int calculateRealMaxCount(List<String> rawArguments, Range range, int routePosition) {
        int maxArguments = range.getMax() == Integer.MAX_VALUE
            ? rawArguments.size()
            : routePosition + range.getMax();

        return Math.min(maxArguments, rawArguments.size());
    }

    private int calculateMaxCount(Range range) {
        if (range.getMax() == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return range.getMax() + pivotPosition;
    }

}