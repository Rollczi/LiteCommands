package dev.rollczi.litecommands.modern.command;

public interface CommandExecuteResultHandler<SENDER, T> {

    void handle(Invocation<SENDER> invocation, T result);

}
