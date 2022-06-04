package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.execute.ExecuteResult;

@FunctionalInterface
public interface ExecuteListener<SENDER> {

    ExecuteResult execute(SENDER sender, LiteInvocation invocation);

}
