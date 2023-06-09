package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;

public interface ArgumentSuggester<SENDER, PARSED> extends Rangeable {

    SuggestionResult suggest(Invocation<SENDER> invocation, Argument<PARSED> argument, SuggestionContext suggestion);

}
