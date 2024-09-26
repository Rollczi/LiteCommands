package dev.rollczi.litecommands.requirement;

import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface RequirementFutureResult<PARSED> {

    CompletableFuture<RequirementResult<PARSED>> asFuture();

    RequirementResult<PARSED> asResultOr(RequirementResult<PARSED> result);

    RequirementResult<PARSED> await();

}
