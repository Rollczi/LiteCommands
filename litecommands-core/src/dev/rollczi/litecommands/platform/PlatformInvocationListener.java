package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.argument.input.ArgumentsInput;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.Invocation;

@FunctionalInterface
public interface PlatformInvocationListener<SENDER> {

    CommandExecuteResult execute(Invocation<SENDER> invocation, ArgumentsInput<?> arguments);

}
