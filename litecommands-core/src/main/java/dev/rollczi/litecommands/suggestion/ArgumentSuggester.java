package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Rangeable;

public interface ArgumentSuggester<SENDER, EXPECTED, ARGUMENT extends Argument<EXPECTED>> extends Rangeable {

    SuggestionResult suggest(Invocation<SENDER> invocation, ARGUMENT argument, SuggestionContext suggestion);

}
