package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.priority.MutablePrioritizedList;
import dev.rollczi.litecommands.shared.FailedReason;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CommandExecutorNotFoundEvent implements Event {

    private final Invocation<?> invocation;
    private final CommandRoute<?> commandRoute;
    private final MutablePrioritizedList<FailedReason> failedReasons;

    public CommandExecutorNotFoundEvent(Invocation<?> invocation, CommandRoute<?> commandRoute, MutablePrioritizedList<FailedReason> failedReasons) {
        this.invocation = invocation;
        this.commandRoute = commandRoute;
        this.failedReasons = failedReasons;
    }

    public Invocation<?> getInvocation() {
        return invocation;
    }

    public CommandRoute<?> getCommandRoute() {
        return commandRoute;
    }

    public void setFailedReason(FailedReason reason) {
        failedReasons.add(reason);
    }

    public FailedReason getFailedReason() {
        return failedReasons.first();
    }

}
