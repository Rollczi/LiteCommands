package dev.rollczi.litecommands.input.raw;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.shared.Preconditions;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public class RawInputAnalyzer {

    private final List<String> rawArguments;
    private int lastPivotPosition = 0;
    private int pivotPosition = 0;
    private boolean isLastOptionalArgument = false;

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
        this.lastPivotPosition = this.pivotPosition;
        this.pivotPosition = pivotPosition;
    }

    public boolean hasNextRoute() {
        return pivotPosition < rawArguments.size();
    }

    public <SENDER, T> Context<SENDER, T> toContext(
        Argument<T> argument,
        Parser<SENDER, T> parser
    ) {
        return new Context<>(argument, parser);
    }

    public String showNextRoute() {
        return rawArguments.get(pivotPosition);
    }

    public String nextRoute() {
        return rawArguments.get(pivotPosition++);
    }

    public List<String> getLastArgumentsBeforePivotMove() {
       return rawArguments.subList(lastPivotPosition, pivotPosition);
    }

    public boolean nextRouteIsLast() {
        return pivotPosition == rawArguments.size() - 1;
    }

    @ApiStatus.Experimental
    public void setLastOptionalArgument(boolean setLastOptionalArgument) {
        this.isLastOptionalArgument = setLastOptionalArgument;
    }

    public void consumeAll() {
        setPivotPosition(rawArguments.size());
    }

    public class Context<SENDER, T> {

        private final Parser<SENDER, T> parser;
        private final Argument<T> argument;
        private final int argumentMinCount;
        private final int argumentMaxCount;
        private final int realArgumentMaxCount;
        private final int realArgumentMinCount;

        public Context(
            Argument<T> argument,
            Parser<SENDER, T> parser
        ) {
            this.argument = argument;
            this.parser = parser;
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

        @ApiStatus.Experimental
        public boolean matchArgument(Invocation<SENDER> invocation) {
            List<String> arguments = rawArguments.subList(pivotPosition, realArgumentMaxCount);
            RawInput input = RawInput.of(arguments);
            boolean parse = parser.match(invocation, argument, input);

            pivotPosition += input.consumedCount();

            return parse;
        }

        public boolean isLastArgument() {
            return pivotPosition == rawArguments.size() - 1 || realArgumentMaxCount >= rawArguments.size() || isPotentialLastArgument() || isLastOptionalArgument;
        }

        public boolean isMissingFullArgument() {
            return isNoMoreArguments() && isMissingPartOfArgument();
        }

        private boolean isNoMoreArguments() {
            return pivotPosition >= rawArguments.size();
        }

        public boolean isMissingPartOfArgument() {
            return argumentMinCount != realArgumentMinCount;
        }

        private boolean isPotentialLastArgument() {
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