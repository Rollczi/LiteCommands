package dev.rollczi.litecommands.argument.type;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.input.ArgumentRawInputParser;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.ArgumentSuggester;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public interface MultipleArgumentResolver<SENDER, TYPE> extends
    ArgumentSuggester<SENDER, TYPE, Argument<TYPE>>,
        ArgumentRawInputParser<SENDER, TYPE> {

    @Override
    default SuggestionResult suggest(Invocation<SENDER> invocation, Argument<TYPE> argument, SuggestionContext suggestion) {
        return SuggestionResult.of();
    }

}
