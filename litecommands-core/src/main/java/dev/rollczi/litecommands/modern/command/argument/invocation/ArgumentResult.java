package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ArgumentResult<EXPECTED> {

    private final @Nullable SuccessfulResult<EXPECTED> successfulResult;
    private final @Nullable FailedReason failedResult;

    private ArgumentResult(@Nullable SuccessfulResult<EXPECTED> successfulResult, @Nullable FailedReason failedResult) {
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
        return successfulResult != null;
    }

    public boolean isFailed() {
        return failedResult != null;
    }

    public SuccessfulResult<EXPECTED> getSuccessfulResult() {
        if (successfulResult == null) {
            throw new IllegalStateException("Cannot get successful result when it is empty");
        }

        return successfulResult;
    }

    public FailedReason getFailedReason() {
        if (failedResult == null) {
            throw new IllegalStateException("Cannot get failed reason when it is empty");
        }

        return failedResult;
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> successMultilevel(ExpectedContextualProvider<EXPECTED> expectedReturn, int consumedRawArguments) {
        return new ArgumentResult<>(SuccessfulResult.of(expectedReturn, consumedRawArguments), null);
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> success(ExpectedContextualProvider<EXPECTED> expectedReturn) {
        return new ArgumentResult<>(SuccessfulResult.of(expectedReturn, 1), null);
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> successOptional(ExpectedContextualProvider<EXPECTED> expectedReturn) {
        return new ArgumentResult<>(SuccessfulResult.optionalArgument(expectedReturn), null);
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> failure(FailedReason failedReason) {
        return new ArgumentResult<>(null, failedReason);
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> failure() {
        return new ArgumentResult<>(null, FailedReason.empty());
    }

}
