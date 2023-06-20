package dev.rollczi.litecommands.argument.parser.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.ParserSet;
import dev.rollczi.litecommands.input.InputMatcher;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.FailedReason;

public interface ParsableInputMatcher<SELF extends ParsableInputMatcher<SELF>> extends InputMatcher {

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
