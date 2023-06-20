package dev.rollczi.litecommands.argument.resolver.std;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.argument.suggestion.SuggestionContext;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;

public class StringArgumentResolver<SENDER> extends ArgumentResolver<SENDER, String> {

    @Override
    protected ParseResult<String> parse(Invocation<SENDER> invocation, Argument<String> context, String argument) {
        return ParseResult.success(() -> argument);
    }

    @Override
    protected boolean canParse(Invocation<SENDER> invocation, Argument<String> context, String argument) {
        return true;
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> objectStringArgument, SuggestionContext context) {
        return SuggestionResult.of("text", "");
    }


}
