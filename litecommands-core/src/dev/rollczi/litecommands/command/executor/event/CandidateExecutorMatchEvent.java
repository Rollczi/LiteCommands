package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.invocation.Invocation;

public class CandidateExecutorMatchEvent extends AbstractCommandExecutorEvent {

    private final CommandExecutorMatchResult matchResult;

    public CandidateExecutorMatchEvent(Invocation<?> invocation, CommandExecutor<?> executor, CommandExecutorMatchResult matchResult) {
        super(invocation, executor);
        this.matchResult = matchResult;
    }

    public CommandExecutorMatchResult getMatchResult() {
        return matchResult;
    }

}
