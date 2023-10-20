package dev.rollczi.litecommands.argument.resolver.standard;

import static dev.rollczi.litecommands.argument.parser.ParseResult.failure;
import static dev.rollczi.litecommands.argument.parser.ParseResult.success;
import static dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause.INVALID_ARGUMENT;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.time.TemporalAmountParser;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.function.Supplier;

class TemporalAmountArgumentResolver<SENDER, UNIT extends TemporalAmount> extends ArgumentResolver<SENDER, UNIT> {

    private final TemporalAmountParser<UNIT> parser;
    private final Supplier<List<String>> suggestions;

    protected TemporalAmountArgumentResolver(TemporalAmountParser<UNIT> parser, Supplier<List<String>> suggestions) {
        this.parser = parser;
        this.suggestions = suggestions;
    }

    @Override
    protected ParseResult<UNIT> parse(Invocation<SENDER> invocation, Argument<UNIT> context, String argument) {
        try {
            return success(parser.parse(argument));
        } catch (IllegalArgumentException exception) {
            return failure(INVALID_ARGUMENT);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<UNIT> argument, SuggestionContext context) {
        return SuggestionResult.of(suggestions.get());
    }
}
