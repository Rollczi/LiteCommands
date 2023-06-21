package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.wrapper.ValueToWrap;
import org.jetbrains.annotations.Nullable;

public class ParseResult<EXPECTED> {

    private final @Nullable EXPECTED successfulResult;
    private final @Nullable FailedReason failedResult;

    private ParseResult(@Nullable EXPECTED successfulResult, @Nullable FailedReason failedResult) {
        if (successfulResult != null && failedResult != null) {
            throw new IllegalArgumentException("Cannot be both successful and failed");
        }

        if (successfulResult == null && failedResult == null) {
            throw new IllegalArgumentException("Cannot be both empty");
        }

        this.successfulResult = successfulResult;
        this.failedResult = failedResult;
    }

    public boolean isSuccessful() {
        return this.successfulResult != null;
    }

    public boolean isFailed() {
        return this.failedResult != null;
    }

    public EXPECTED getSuccessfulResult() {
        if (this.successfulResult == null) {
            throw new IllegalStateException("Cannot get successful result when it is empty");
        }

        return this.successfulResult;
    }

    public FailedReason getFailedReason() {
        if (this.failedResult == null) {
            throw new IllegalStateException("Cannot get failed reason when it is empty");
        }

        return this.failedResult;
    }

    public static <PARSED> ParseResult<PARSED> success(PARSED parsed) {
        return new ParseResult<>(parsed, null);
    }

    public static <EXPECTED> ParseResult<EXPECTED> failure(FailedReason failedReason) {
        return new ParseResult<>(null, failedReason);
    }

    public static <EXPECTED> ParseResult<EXPECTED> failure(Object failedReason) {
        return new ParseResult<>(null, FailedReason.of(failedReason));
    }

    public static <EXPECTED> ParseResult<EXPECTED> failure() {
        return new ParseResult<>(null, FailedReason.empty());
    }

}
