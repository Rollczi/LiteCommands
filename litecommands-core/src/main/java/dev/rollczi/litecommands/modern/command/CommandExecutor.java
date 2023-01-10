package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentSet;

public interface CommandExecutor extends Iterable<ArgumentContext<?, ?>> {

    <SENDER> CommandExecuteResult execute(Invocation<SENDER> invocation, WrappedArgumentSet arguments);

}
