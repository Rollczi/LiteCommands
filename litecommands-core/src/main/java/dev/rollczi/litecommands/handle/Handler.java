package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.LiteInvocation;

public interface Handler<SENDER, T> {

    void handle(SENDER sender, LiteInvocation invocation, T value);

}
