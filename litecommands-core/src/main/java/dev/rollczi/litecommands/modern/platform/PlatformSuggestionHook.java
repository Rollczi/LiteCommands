package dev.rollczi.litecommands.modern.platform;

import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResult;

@FunctionalInterface
public interface PlatformSuggestionHook<SENDER> {
    
    SuggestionResult suggest(Invocation<SENDER> invocation);
    
}
