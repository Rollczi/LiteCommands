package dev.rollczi.litecommands.modern.argument.invocation;

import panda.std.Result;

public class ArgumentResult<EXPECTED> {

    private final Result<SuccessfulResult<EXPECTED>, FailedReason> resultOfParsedArguments;

    private ArgumentResult(Result<SuccessfulResult<EXPECTED>, FailedReason> resultOfParsedArguments) {
        this.resultOfParsedArguments = resultOfParsedArguments;
    }

    public boolean isSuccessful() {
        return resultOfParsedArguments.isOk();
    }

    public boolean isFailed() {
        return resultOfParsedArguments.isErr();
    }

    public SuccessfulResult<EXPECTED> getSuccessfulResult() {
        if (resultOfParsedArguments.isErr()) {
            throw new IllegalStateException("Cannot get successful result when argument is not matched");
        }

        return resultOfParsedArguments.get();
    }

    public FailedReason getFailedReason() {
        if (resultOfParsedArguments.isOk()) {
            throw new IllegalStateException("Cannot get failed result when argument is matched");
        }

        return resultOfParsedArguments.getError();
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> success(EXPECTED expectedReturn, int consumedRawArguments) {
        return new ArgumentResult<>(Result.ok(SuccessfulResult.of(expectedReturn, consumedRawArguments)));
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> success(EXPECTED expectedReturn, int consumedRawArguments) {
        return new ArgumentResult<>(Result.ok(SuccessfulResult.of(expectedReturn, consumedRawArguments)));
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> failure(FailedReason failedReason) {
        return new ArgumentResult<>(Result.error(failedReason));
    }

    public static <EXPECTED> ArgumentResult<EXPECTED> failure() {
        return new ArgumentResult<>(Result.error(FailedReason.empty()));
    }

}
