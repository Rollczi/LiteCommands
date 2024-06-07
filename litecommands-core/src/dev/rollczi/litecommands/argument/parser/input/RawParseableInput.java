package dev.rollczi.litecommands.argument.parser.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.input.raw.RawInputAnalyzer;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class RawParseableInput implements ParseableInput<RawParseableInput.RawInputMatcher> {

    private final List<String> rawArguments;

    public RawParseableInput(List<String> rawArguments) {
        this.rawArguments = new ArrayList<>(rawArguments);
    }

    @Override
    public RawInputMatcher createMatcher() {
        return new RawInputMatcher();
    }

    @Override
    public List<String> asList() {
        return Collections.unmodifiableList(this.rawArguments);
    }

    public class RawInputMatcher implements ParseableInputMatcher<RawInputMatcher> {

        private final RawInputAnalyzer rawInputAnalyzer = new RawInputAnalyzer(rawArguments);

        public RawInputMatcher() {}

        public RawInputMatcher(int pivotPosition) {
            this.rawInputAnalyzer.setPivotPosition(pivotPosition);
        }

        @Override
        public <SENDER, PARSED> ParseResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, Parser<SENDER, PARSED> parser) {
            RawInputAnalyzer.Context<SENDER, PARSED> context = rawInputAnalyzer.toContext(argument, parser);

            if (context.isMissingFullArgument()) {
                Optional<ParseResult<PARSED>> optional = argument.defaultValue();

                return optional
                    .orElseGet(() -> ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT));
            }

            if (context.isMissingPartOfArgument()) {
                return ParseResult.failure(InvalidUsage.Cause.MISSING_PART_OF_ARGUMENT);
            }

            return context.parseArgument(invocation);
        }

        @Override
        public boolean hasNextRoute() {
            return rawInputAnalyzer.hasNextRoute();
        }

        @Override
        public String showNextRoute() {
            return rawInputAnalyzer.showNextRoute();
        }

        @Override
        public String nextRoute() {
            return rawInputAnalyzer.nextRoute();
        }

        @Override
        public RawInputMatcher copy() {
            return new RawInputMatcher(rawInputAnalyzer.getPivotPosition());
        }

        @Override
        public EndResult endMatch() {
            if (rawInputAnalyzer.getPivotPosition() < rawArguments.size()) {
                return EndResult.failed(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
            }

            return EndResult.success();
        }

    }

}
