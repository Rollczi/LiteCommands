package dev.rollczi.litecommands.requirement;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface RequirementResult<PARSED> {

    boolean isSuccessful();

    boolean isSuccessfulNull();

    boolean isFailed();

    @NotNull
    PARSED getSuccess();

    @NotNull
    Object getFailedReason();

    @NotNull
    @ApiStatus.Experimental
    List<RequirementCondition> getConditions();

}
