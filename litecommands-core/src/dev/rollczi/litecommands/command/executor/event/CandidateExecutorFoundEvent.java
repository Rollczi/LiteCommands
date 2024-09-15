package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.invocation.Invocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CandidateExecutorFoundEvent<SENDER> extends AbstractCommandExecutorEvent<SENDER> {

    public CandidateExecutorFoundEvent(Invocation<SENDER> invocation, CommandExecutor<SENDER> executor) {
        super(invocation, executor);
    }

}
