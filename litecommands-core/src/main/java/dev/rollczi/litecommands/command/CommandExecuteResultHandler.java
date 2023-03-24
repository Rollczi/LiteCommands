package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.invocation.Invocation;

public interface CommandExecuteResultHandler<SENDER, T> {

    void handle(Invocation<SENDER> invocation, T result);

}
