package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.invocation.Invocation;

public interface CommandExecuteResultHandler<SENDER, T> {

    void handle(Invocation<SENDER> invocation, T result);

}
