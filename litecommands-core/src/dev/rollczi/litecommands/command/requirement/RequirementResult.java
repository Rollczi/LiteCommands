package dev.rollczi.litecommands.command.requirement;

import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.wrapper.Wrap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RequirementResult<PARSED> {

    private final @Nullable Wrap<PARSED> success;
    private final @Nullable Object error;

    private RequirementResult(@Nullable Wrap<PARSED> success, @Nullable Object error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success != null;
    }

    public boolean isFailure() {
        return error != null;
    }

    @NotNull
    public Wrap<PARSED> getSuccess() {
        if (success == null) {
            throw new IllegalStateException("Cannot get success when result is failed");
        }

        return success;
    }

    @NotNull
    public Object getError() {
        if (error == null) {
            throw new IllegalStateException("Cannot get failed reason when result is success");
        }

        return error;
    }

    public static <EXPECTED> RequirementResult<EXPECTED> success(Wrap<EXPECTED> wrappedExpected) {
        return new RequirementResult<>(wrappedExpected, null);
    }

    public static <EXPECTED> RequirementResult<EXPECTED> failure(FailedReason failedReason) {
        return new RequirementResult<>(null, failedReason.getReasonOr(null));
    }

    public static <EXPECTED> RequirementResult<EXPECTED> failure(Object failedReason) {
        return new RequirementResult<>(null, failedReason);
    }

}
