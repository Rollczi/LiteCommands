package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.SchedulerType;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

class ScheduledRequirement<T> {

    private final Requirement<T> requirement;
    private final Supplier<CompletableFuture<RequirementFutureResult<?>>> match;

    public ScheduledRequirement(Requirement<T> requirement, Supplier<CompletableFuture<RequirementFutureResult<?>>> match) {
        this.requirement = requirement;
        this.match = match;
    }

    public Requirement<T> getRequirement() {
        return requirement;
    }

    public CompletableFuture<RequirementFutureResult<?>> runMatch() {
        return match.get();
    }

    public SchedulerType type() {
        return requirement.meta().get(Meta.POLL_TYPE);
    }


}
