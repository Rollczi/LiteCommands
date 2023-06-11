package dev.rollczi.litecommands.argument.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ArgumentRawInputParser;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggester;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public interface MultipleArgumentResolver<SENDER, TYPE> extends
    Suggester<SENDER, TYPE>,
    ArgumentRawInputParser<SENDER, TYPE> {

    @Override
    default SuggestionResult suggest(Invocation<SENDER> invocation, Argument<TYPE> argument, SuggestionContext context) {
        return SuggestionResult.of();
    }

}
