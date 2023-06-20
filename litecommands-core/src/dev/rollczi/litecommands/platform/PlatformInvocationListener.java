package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandExecuteResult;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface PlatformInvocationListener<SENDER> {

    CompletableFuture<CommandExecuteResult> execute(Invocation<SENDER> invocation, ParseableInput<?> arguments);

}
