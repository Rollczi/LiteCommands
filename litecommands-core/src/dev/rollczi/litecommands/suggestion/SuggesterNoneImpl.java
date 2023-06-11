package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;

class SuggesterNoneImpl<SENDER, T> implements Suggester<SENDER, T> {

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context) {
        return SuggestionResult.empty();
    }

}
