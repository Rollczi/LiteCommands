package dev.rollczi.litecommands.modern.command.filter;

import dev.rollczi.litecommands.modern.command.CommandExecutor;
import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;

public interface CommandFilter<SENDER> {

    boolean test(Invocation<SENDER> invocation, CommandRoute command, CommandExecutor executor);

}
