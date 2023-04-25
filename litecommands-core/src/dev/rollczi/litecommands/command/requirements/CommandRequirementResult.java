package dev.rollczi.litecommands.command.requirements;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.wrapper.Wrapped;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CommandRequirementResult<OUT> {

    private final @Nullable Supplier<Wrapped<OUT>> success;
    private final @Nullable FailedReason failedReason;

    private CommandRequirementResult(@Nullable Supplier<Wrapped<OUT>> success, @Nullable FailedReason failedReason) {
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
    public Wrapped<OUT> getSuccess() {
        if (success == null) {
            throw new IllegalStateException("Cannot get success when result is failed");
        }

        return success.get();
    }

    @NotNull
    public FailedReason getFailedReason() {
        if (failedReason == null) {
            throw new IllegalStateException("Cannot get failed reason when result is success");
        }

        return failedReason;
    }

    public static <EXPECTED> CommandRequirementResult<EXPECTED> success(Supplier<Wrapped<EXPECTED>> wrappedExpected) {
        return new CommandRequirementResult<>(wrappedExpected, null);
    }

    public static <EXPECTED> CommandRequirementResult<EXPECTED> failure(FailedReason failedReason) {
        return new CommandRequirementResult<>(null, failedReason);
    }

    public static <EXPECTED> CommandRequirementResult<EXPECTED> failure(Object failedReason) {
        return new CommandRequirementResult<>(null, FailedReason.of(failedReason));
    }

}
