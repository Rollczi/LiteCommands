package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

@FunctionalInterface
public interface PlatformSuggestionHook<SENDER> {
    
    SuggestionResult suggest(Invocation<SENDER> invocation);
    
}
