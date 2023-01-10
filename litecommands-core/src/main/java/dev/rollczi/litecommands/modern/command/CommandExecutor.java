package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import panda.std.Result;

public interface CommandExecutor extends Iterable<ArgumentContext<?, ?>> {

    <SENDER> Result<Object, FailedReason> execute(ArgumentResultCollector<SENDER> invocation);

}
