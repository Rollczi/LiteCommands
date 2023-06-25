package dev.rollczi.litecommands.argument.suggestion;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;

@SuppressWarnings("rawtypes")
public interface TypedSuggester<SENDER, PARSED, ARGUMENT extends Argument<PARSED>> extends Suggester<SENDER, PARSED> {

    SuggestionResult suggestTyped(Invocation<SENDER> invocation, ARGUMENT argument, SuggestionContext context);

    Class<? extends Argument> getArgumentType();

    @Override
    @SuppressWarnings("unchecked")
    default SuggestionResult suggest(Invocation<SENDER> invocation, Argument<PARSED> argument, SuggestionContext context) {
        return suggestTyped(invocation, (ARGUMENT) argument, context);
    }

}
