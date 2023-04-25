package dev.rollczi.litecommands.argument.input;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.invocation.Invocation;

public interface InputArgumentsMatcher<C extends InputArgumentsMatcher<C>> {

    <SENDER, PARSED> ArgumentResult<PARSED> matchArgument(Invocation<SENDER> invocation, Argument<PARSED> argument, ArgumentParserSet<SENDER, PARSED> parserSet);

    boolean hasNextRoute();

    String matchNextRoute();

    String showNextRoute();

    C copy();

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
