package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.suggester.SuggesterChainAccessor;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface MultipleArgumentResolverChained<SENDER, PARSED> extends ArgumentResolverBaseChained<SENDER, PARSED> {

    @Override
    default SuggestionResult suggest(Invocation<SENDER> invocation, Argument<PARSED> argument, SuggestionContext context, SuggesterChainAccessor<SENDER> chainAccessor) {
        return SuggestionResult.empty();
    }

}
