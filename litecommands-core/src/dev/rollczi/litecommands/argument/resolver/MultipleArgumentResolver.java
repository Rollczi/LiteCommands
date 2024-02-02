package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public interface MultipleArgumentResolver<SENDER, TYPE> extends
    ArgumentResolverBase<SENDER, TYPE>,
    Suggester<SENDER, TYPE>,
    Parser<SENDER, TYPE> {

    @Override
    default SuggestionResult suggest(Invocation<SENDER> invocation, Argument<TYPE> argument, SuggestionContext context) {
        return SuggestionResult.of();
    }

}
