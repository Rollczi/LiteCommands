package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public interface Suggester<SENDER, PARSED> {

    SuggestionResult suggest(Invocation<SENDER> invocation, Argument<PARSED> argument, SuggestionContext context);

    static <SENDER, PARSED> SuggesterNone<SENDER, PARSED> none() {
        return new SuggesterNone<>();
    }

}
