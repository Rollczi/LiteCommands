package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;

@FunctionalInterface
public interface ExecuteListener<SENDER> {

    ExecuteResult execute(SENDER sender, LiteInvocation invocation);

}
