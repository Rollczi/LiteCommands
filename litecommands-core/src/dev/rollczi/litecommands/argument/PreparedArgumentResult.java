package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.WrappedExpected;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PreparedArgumentResult<EXPECTED> {

    private final @Nullable Success<EXPECTED> success;
    private final @Nullable FailedReason failedReason;

    private PreparedArgumentResult(@Nullable Success<EXPECTED> success, @Nullable FailedReason failedReason) {
        this.success = success;
        this.failedReason = failedReason;
    }

    public boolean isSuccess() {
        return success != null;
    }

    public boolean isFailed() {
        return failedReason != null;
    }

    @NotNull
    public Success<EXPECTED> getSuccess() {
        if (success == null) {
            throw new IllegalStateException("Cannot get success when result is failed");
        }

        return success;
    }

    @NotNull
    public FailedReason getFailedReason() {
        if (failedReason == null) {
            throw new IllegalStateException("Cannot get failed reason when result is success");
        }

        return failedReason;
    }

    public static <EXPECTED> PreparedArgumentResult<EXPECTED> success(Supplier<WrappedExpected<EXPECTED>> wrappedExpected, int consumedRawArguments) {
        return new PreparedArgumentResult<>(new Success<>(wrappedExpected, consumedRawArguments), null);
    }

    public static <EXPECTED> PreparedArgumentResult<EXPECTED> failed(FailedReason failedReason) {
        return new PreparedArgumentResult<>(null, failedReason);
    }

    public static <EXPECTED> PreparedArgumentResult<EXPECTED> failed(Object failedReason) {
        return new PreparedArgumentResult<>(null, FailedReason.of(failedReason));
    }

    public static class Success<EXPECTED> {
        private final Supplier<WrappedExpected<EXPECTED>> wrappedExpected;
        private final int consumedRawArguments;

        public Success(Supplier<WrappedExpected<EXPECTED>> wrappedExpected, int consumedRawArguments) {
            this.wrappedExpected = wrappedExpected;
            this.consumedRawArguments = consumedRawArguments;
        }

        public Supplier<WrappedExpected<EXPECTED>> getWrappedExpected() {
            return wrappedExpected;
        }

        public int getConsumedRawArguments() {
            return consumedRawArguments;
        }
    }

}
