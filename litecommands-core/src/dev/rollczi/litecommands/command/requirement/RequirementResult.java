package dev.rollczi.litecommands.command.requirement;

import dev.rollczi.litecommands.argument.FailedReason;
import dev.rollczi.litecommands.wrapper.Wrap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RequirementResult<PARSED> {

    private final @Nullable Supplier<Wrap<PARSED>> success;
    private final @Nullable FailedReason failedReason;

    private RequirementResult(@Nullable Supplier<Wrap<PARSED>> success, @Nullable FailedReason failedReason) {
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
    public Wrap<PARSED> getSuccess() {
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

    public static <EXPECTED> RequirementResult<EXPECTED> success(Supplier<Wrap<EXPECTED>> wrappedExpected) {
        return new RequirementResult<>(wrappedExpected, null);
    }

    public static <EXPECTED> RequirementResult<EXPECTED> failure(FailedReason failedReason) {
        return new RequirementResult<>(null, failedReason);
    }

    public static <EXPECTED> RequirementResult<EXPECTED> failure(Object failedReason) {
        return new RequirementResult<>(null, FailedReason.of(failedReason));
    }

}
