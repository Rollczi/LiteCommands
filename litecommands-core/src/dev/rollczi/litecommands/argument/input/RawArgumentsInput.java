package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.parser.ArgumentParserSet;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RawArgumentsInput implements ArgumentsInput<RawArgumentsInput.RawInputMatcher> {

    private final List<String> rawArguments;

    public RawArgumentsInput(List<String> rawArguments) {
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

    public class RawInputMatcher implements ArgumentsInputMatcher<RawInputMatcher> {

        private final RawInputAnalyzer rawInputAnalyzer = new RawInputAnalyzer(rawArguments);

        public RawInputMatcher() {}

        public RawInputMatcher(int pivotPosition) {
            this.rawInputAnalyzer.setPivotPosition(pivotPosition);
        }

        @Override
        public <SENDER, PARSED> ArgumentResult<PARSED> nextArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ArgumentParserSet<SENDER, PARSED> parserSet) {
            RawInputAnalyzer.Context<SENDER, PARSED> context = rawInputAnalyzer.toContext(argument, parserSet);

            if (context.isMissingFullArgument()) {
                return ArgumentResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
            }

            if (context.isMissingPartOfArgument()) {
                return ArgumentResult.failure(InvalidUsage.Cause.TOO_FEW_ARGUMENTS);
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
