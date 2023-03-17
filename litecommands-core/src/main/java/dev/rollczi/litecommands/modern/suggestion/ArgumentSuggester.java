package dev.rollczi.litecommands.modern.suggestion;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.range.Rangeable;
import dev.rollczi.litecommands.modern.invocation.Invocation;

public interface ArgumentSuggester<SENDER, EXPECTED, ARGUMENT extends Argument<EXPECTED>> extends Rangeable {

    SuggestionResult suggest(Invocation<SENDER> invocation, ARGUMENT argument, SuggestionContext suggestion);

}
