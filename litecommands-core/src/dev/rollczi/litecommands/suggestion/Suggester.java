package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;

public interface Suggester<SENDER, PARSED> {

    SuggestionResult suggest(Invocation<SENDER> invocation, Argument<PARSED> argument, SuggestionContext context);

}
