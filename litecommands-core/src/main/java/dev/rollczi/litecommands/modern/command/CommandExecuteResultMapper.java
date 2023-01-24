package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.invocation.Invocation;

public interface CommandExecuteResultMapper<SENDER, FROM, TO> {

    TO map(Invocation<SENDER> invocation, FROM from);

}
