package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.command.Invocation;

@FunctionalInterface
public interface PlatformCommandSuggestListener<SENDER> {
    
    void execute(Invocation<SENDER> invocation);
    
}
