package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;

public interface ArgumentTypedSuggester<SENDER, PARSED, ARGUMENT extends Argument<PARSED>> extends ArgumentSuggester<SENDER, PARSED> {

    SuggestionResult suggestTyped(Invocation<SENDER> invocation, ARGUMENT argument, SuggestionContext suggestion);

    Class<? extends Argument> getArgumentType();

    @Override
    @SuppressWarnings("unchecked")
    default SuggestionResult suggest(Invocation<SENDER> invocation, Argument<PARSED> argument, SuggestionContext suggestion) {
        return suggestTyped(invocation, (ARGUMENT) argument, suggestion);
    }

}
