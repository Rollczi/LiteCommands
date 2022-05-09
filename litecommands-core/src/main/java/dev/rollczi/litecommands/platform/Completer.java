package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.CompletionResult;
import dev.rollczi.litecommands.command.LiteInvocation;

@FunctionalInterface
public interface Completer<SENDER> {

    CompletionResult completion(SENDER sender, LiteInvocation invocation);

}
