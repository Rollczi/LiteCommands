package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.command.Invocation;

@FunctionalInterface
public interface PlatformCommandExecuteListener<SENDER> {

    void execute(Invocation<SENDER> invocation);

}
