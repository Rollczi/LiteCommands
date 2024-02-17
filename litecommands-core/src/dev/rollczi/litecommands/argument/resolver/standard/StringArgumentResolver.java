package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public class StringArgumentResolver<SENDER> extends ArgumentResolver<SENDER, String> {

    @Override
    protected ParseResult<String> parse(Invocation<SENDER> invocation, Argument<String> context, String argument) {
        return ParseResult.success(argument);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> objectStringArgument, SuggestionContext context) {
        String first = context.getCurrent().multilevel();

        if (first.isEmpty()) {
            return SuggestionResult.of("<" + objectStringArgument.getName() + ">");
        }

        return SuggestionResult.of("<" + objectStringArgument.getName() + ">", first);
    }
    
}
