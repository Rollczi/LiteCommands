package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.invocation.Invocation;

public interface CommandExecuteResultMapper<SENDER, FROM, TO> {

    TO map(Invocation<SENDER> invocation, FROM from);

}
