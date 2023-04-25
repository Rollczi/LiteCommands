package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.range.Rangeable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RawInputArguments implements InputArguments<RawInputArguments.RawInputMatcher> {

    private final List<String> rawArguments;

    public RawInputArguments(List<String> rawArguments) {
        this.rawArguments = new ArrayList<>(rawArguments);
    }

    @Override
    public RawInputMatcher createMatcher() {
        return new RawInputMatcher();
    }

    @Override
    public String[] asArray() {
        return this.rawArguments.toArray(new String[0]);
    }

    @Override
    public List<String> asList() {
        return Collections.unmodifiableList(this.rawArguments);
    }

    public class RawInputMatcher implements InputArgumentsMatcher<RawInputMatcher> {

        private int pivotPosition = 0;
        private int argumentsParsed = 0;

        public RawInputMatcher() {}

        public RawInputMatcher(int pivotPosition, int argumentsParsed) {
            this.pivotPosition = pivotPosition;
            this.argumentsParsed = argumentsParsed;
        }

        public int pivotPosition() {
            return this.pivotPosition;
        }

        void movePivot(int amount) {
            this.pivotPosition += amount;
        }

        @Override
        public <SENDER, PARSED> ArgumentResult<PARSED> matchArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ArgumentParserSet<SENDER, PARSED> parserSet) {
            ArgumentParser<SENDER, RawInput, PARSED> parser = parserSet.getParser(RawInput.class)
                .orElseThrow(() -> new ArgumentParseException("No parser for RawInput -> " + argument.getWrapperFormat().getType().getName()));

            if (!(parser instanceof Rangeable)) {
                throw new ArgumentParseException("Parser must be Rangeable");
            }

            return this.match(invocation, argument, (ArgumentParser<SENDER, RawInput, PARSED> & Rangeable) parser);
        }

        private <PARSER extends ArgumentParser<SENDER, RawInput, PARSED> & Rangeable, SENDER, PARSED> ArgumentResult<PARSED> match(
            Invocation<SENDER> invocation,
            Argument<PARSED> argument,
            PARSER parser
        ) {
            Range range = parser.getRange();
            int routePosition = this.pivotPosition();

            int minArguments = range.getMin();
            int maxArguments = range.getMax() == Integer.MAX_VALUE
                ? rawArguments.size()
                : routePosition + range.getMax();

            maxArguments = Math.min(maxArguments, rawArguments.size());

            if (minArguments > rawArguments.size()) {
                return ArgumentResult.failure(InvalidUsage.Cause.TOO_FEW_ARGUMENTS);
            }

            List<String> arguments = rawArguments.subList(routePosition, maxArguments);
            RawInput input = RawInput.of(arguments);
            ArgumentResult<PARSED> parsed = parser.parse(invocation, argument, input);

            this.movePivot(input.consumedCount());

            return parsed;
        }

        @Override
        public boolean hasNextRoute() {
            return this.pivotPosition < rawArguments.size();
        }

        @Override
        public String showNextRoute() {
            return rawArguments.get(this.pivotPosition);
        }

        @Override
        public String matchNextRoute() {
            return rawArguments.get(this.pivotPosition++);
        }

        @Override
        public RawInputMatcher copy() {
            return new RawInputMatcher(this.pivotPosition, this.argumentsParsed);
        }

        @Override
        public EndResult endMatch() {
            if (this.pivotPosition < rawArguments.size()) {
                return EndResult.failed(InvalidUsage.Cause.TOO_FEW_ARGUMENTS);
            }

            return EndResult.success();
        }

    }

}
