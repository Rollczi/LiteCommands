package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public interface Suggester<SENDER, T> {

    SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context);

}
