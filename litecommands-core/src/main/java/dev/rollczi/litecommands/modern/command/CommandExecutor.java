package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.contextual.warpped.ExpectedContextualConverter;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Result;

public interface CommandExecutor {

    CommandExecutorKey getKey();

    <SENDER> Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, ExpectedContextualConverter<SENDER> wrappedExpectedContextualProvider);

}
