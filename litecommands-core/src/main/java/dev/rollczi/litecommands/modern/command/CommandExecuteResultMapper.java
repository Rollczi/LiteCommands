package dev.rollczi.litecommands.modern.command;

public interface CommandExecuteResultMapper<SENDER, FROM, TO> {

    TO map(Invocation<SENDER> invocation, FROM from);

}
