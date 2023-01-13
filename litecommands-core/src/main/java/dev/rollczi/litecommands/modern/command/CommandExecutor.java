package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.WrappedArgumentProvider;
import panda.std.Result;

public interface CommandExecutor {

    <SENDER> Result<CommandExecuteResult, FailedReason> execute(Invocation<SENDER> invocation, WrappedArgumentProvider<SENDER> wrappedArgumentProvider);

}
