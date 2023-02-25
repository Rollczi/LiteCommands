package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.FailedReason;
import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.ExpectedContextualConverter;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import panda.std.Result;

import java.util.List;

public interface CommandExecutor<SENDER> {

    CommandExecutorKey getKey();

    List<ExecutableArgument<SENDER, ?, ?, ?>> arguments();

    List<ExpectedContextual<?>> contextuals();

    Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, ExpectedContextualConverter<SENDER> wrappedExpectedContextualProvider);

}
