package dev.rollczi.litecommands.requirement;

import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface RequirementFutureResult<PARSED> {

    CompletableFuture<? extends RequirementResult<PARSED>> asFuture();

    @SuppressWarnings("unchecked")
    default RequirementResult<PARSED> getNow(RequirementResult<PARSED> valueIfAbsent) {
        if (this instanceof RequirementResult) {
            return (RequirementResult<PARSED>) this;
        }

        CompletableFuture<? extends RequirementResult<PARSED>> completableFuture = asFuture();

        if (completableFuture.isCancelled() || completableFuture.isCompletedExceptionally()) {
            return valueIfAbsent;
        }

        RequirementResult<PARSED> now = completableFuture.getNow(null);

        if (now == null) {
            return valueIfAbsent;
        }

        return now;
    }

    @SuppressWarnings("unchecked")
    default RequirementResult<PARSED> await()  {
        if (this instanceof RequirementResult) {
            return (RequirementResult<PARSED>) this;
        }

        return asFuture().join();
    }

}
