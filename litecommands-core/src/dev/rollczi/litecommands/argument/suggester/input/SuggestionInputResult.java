package dev.rollczi.litecommands.argument.suggester.input;

import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.Nullable;

public class SuggestionInputResult {

    private final Cause cause;
    private final @Nullable SuggestionResult result;

    private SuggestionInputResult(Cause cause, @Nullable SuggestionResult result) {
        this.cause = cause;
        this.result = result;
    }

    public SuggestionResult getResult() {
        return result == null ? SuggestionResult.empty() : result;
    }

    public Cause getCause() {
        return cause;
    }

    public boolean isContinue() {
        return cause == Cause.CONTINUE;
    }

    public boolean isEnd() {
        return cause == Cause.END;
    }

    public boolean isFail() {
        return cause == Cause.FAIL;
    }

    public enum Cause {
        CONTINUE,
        END,
        FAIL,
    }

    public static SuggestionInputResult continueWith(SuggestionResult result) {
        return new SuggestionInputResult(Cause.CONTINUE, result);
    }

    public static SuggestionInputResult continueWith(SuggestionInputResult result) {
        return new SuggestionInputResult(Cause.CONTINUE, result.getResult());
    }

    public static SuggestionInputResult continueWithout() {
        return new SuggestionInputResult(Cause.CONTINUE, null);
    }

    public static SuggestionInputResult endWith(SuggestionResult result) {
        return new SuggestionInputResult(Cause.END, result);
    }

    public static SuggestionInputResult fail() {
        return new SuggestionInputResult(Cause.FAIL, null);
    }

}
