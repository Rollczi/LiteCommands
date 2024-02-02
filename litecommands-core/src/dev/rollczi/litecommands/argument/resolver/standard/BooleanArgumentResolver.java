package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BooleanArgumentResolver<SENDER> extends ArgumentResolver<SENDER, Boolean> {

    private final Set<String> trueValues;
    private final Set<String> falseValues;
    private final SuggestionResult suggestions;

    public BooleanArgumentResolver() {
        this(Collections.singletonList("true"), Collections.singletonList("false"));
    }

    public BooleanArgumentResolver(Collection<String> trueValues, Collection<String> falseValues) {
        this.trueValues = new HashSet<>(trueValues);
        this.falseValues = new HashSet<>(falseValues);
        SuggestionResult suggestionResult = SuggestionResult.empty();

        for (String trueValue : trueValues) {
            suggestionResult.add(Suggestion.of(trueValue));
        }

        for (String falseValue : falseValues) {
            suggestionResult.add(Suggestion.of(falseValue));
        }

        this.suggestions = suggestionResult;
    }


    @Override
    protected ParseResult<Boolean> parse(Invocation<SENDER> invocation, Argument<Boolean> context, String argument) {
        if (trueValues.contains(argument)) {
            return ParseResult.success(true);
        }

        if (falseValues.contains(argument)) {
            return ParseResult.success(false);
        }

        return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Boolean> argument, SuggestionContext context) {
        return suggestions;
    }

}
