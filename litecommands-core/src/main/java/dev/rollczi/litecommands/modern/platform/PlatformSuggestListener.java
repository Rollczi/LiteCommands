package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.invocation.Invocation;

@FunctionalInterface
public interface PlatformSuggestListener<SENDER> {
    
    void execute(Invocation<SENDER> invocation);
    
}
