package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.input.SuggestionInput;

@FunctionalInterface
public interface PlatformSuggestionListener<SENDER> {

    SuggestionResult suggest(Invocation<SENDER> invocation, SuggestionInput<?> suggestion);

}
