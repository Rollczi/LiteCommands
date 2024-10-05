package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface SuggesterChainAccessor<SENDER> {

    <T> SuggestionResult suggest(Invocation<SENDER> invocation, Argument<T> argument, SuggestionContext context);

    default <T> SuggestionResult suggest(Invocation<SENDER> invocation, Class<T> type, SuggestionContext context) {
        return suggest(invocation, Argument.of("default", TypeToken.of(type)), context);
    }

}
