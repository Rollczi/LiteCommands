package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.FailedReason;
import dev.rollczi.litecommands.shared.Preconditions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
public class CommandExecutorFoundEvent<SENDER> implements CommandExecutorEvent<SENDER> {

    private final Invocation<SENDER> invocation;
    private final CommandExecutor<SENDER> executor;

    private CommandExecutorMatchResult matchResult;
    private @Nullable FailedReason cancelReason;

    public CommandExecutorFoundEvent(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor, CommandExecutorMatchResult matchResult) {
        this.matchResult = matchResult;
        this.invocation = invocation;
        this.executor = executor;
    }

    @Override
    public Invocation<SENDER> getInvocation() {
        return invocation;
    }

    @Override
    public CommandExecutor<SENDER> getExecutor() {
        return executor;
    }

    public CommandExecutorMatchResult getResult() {
        return matchResult;
    }

    /**
     * Will skip the execution of the command if the result is failed.
     */
    public void setResult(CommandExecutorMatchResult result) {
        Preconditions.notNull(result, "match result");
        this.matchResult = result;
    }

    /**
     * Will stop the execution of the command.
     */
    public void cancel(FailedReason reason) {
        Preconditions.notNull(reason, "cancel reason");
        this.cancelReason = reason;
    }

    public void allow() {
        this.cancelReason = null;
    }

    public boolean isCancelled() {
        return this.cancelReason != null;
    }

    public @Nullable FailedReason getCancelReason() {
        return cancelReason;
    }

}
