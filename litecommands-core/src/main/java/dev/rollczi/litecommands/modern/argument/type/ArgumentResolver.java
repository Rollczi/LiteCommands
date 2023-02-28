package dev.rollczi.litecommands.modern.argument.type;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.ArgumentSuggester;
import dev.rollczi.litecommands.modern.suggestion.SuggestionContext;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.List;

public interface ArgumentResolver<SENDER, TYPE> extends
    ArgumentParser<SENDER, TYPE, Argument<TYPE>>,
    ArgumentSuggester<SENDER, TYPE, Argument<TYPE>> {

    @Override
    default SuggestionResult suggest(Invocation<SENDER> invocation, Argument<TYPE> argument, SuggestionContext suggestion) {
        return SuggestionResult.of();
    }
}
