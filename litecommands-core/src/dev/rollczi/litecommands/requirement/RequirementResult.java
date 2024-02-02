package dev.rollczi.litecommands.requirement;

import org.jetbrains.annotations.NotNull;

public interface RequirementResult<PARSED> {

    boolean isSuccessful();

    boolean isSuccessfulNull();

    boolean isFailed();

    @NotNull
    PARSED getSuccess();

    @NotNull
    Object getFailedReason();

}
