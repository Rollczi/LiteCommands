package dev.rollczi.litecommands.command.executor.event;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.flow.ExecuteFlowEvent;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.FailedReason;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class CommandExecutionEvent extends AbstractCommandExecutorEvent implements CommandExecutorEvent, ExecuteFlowEvent<FailedReason> {

    public CommandExecutionEvent(Invocation<?> invocation, CommandExecutor<?> executor) {
        super(invocation, executor);
    }

}
