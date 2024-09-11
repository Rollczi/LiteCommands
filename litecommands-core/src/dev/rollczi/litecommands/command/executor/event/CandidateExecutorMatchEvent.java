package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CandidateExecutorMatchEvent<SENDER> extends AbstractCommandExecutorEvent<SENDER> {

    private final CommandExecutorMatchResult matchResult;

    public CandidateExecutorMatchEvent(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor, CommandExecutorMatchResult matchResult) {
        super(invocation, executor);
        this.matchResult = matchResult;
    }

    public CommandExecutorMatchResult getMatchResult() {
        return matchResult;
    }

}
