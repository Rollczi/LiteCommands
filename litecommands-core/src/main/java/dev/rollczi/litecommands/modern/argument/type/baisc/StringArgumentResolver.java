package dev.rollczi.litecommands.modern.argument.type.baisc;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.type.OneArgumentResolver;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.SuggestionContext;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResult;

public class StringArgumentResolver<SENDER> extends OneArgumentResolver<SENDER, String> {

    @Override
    protected ArgumentResult<String> parse(Invocation<SENDER> invocation, Argument<String> context, String argument) {
        return ArgumentResult.success(() -> argument);
    }

    @Override
    protected boolean canParse(Invocation<SENDER> invocation, Argument<String> context, String argument) {
        return false;
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> objectStringArgument, SuggestionContext suggestion) {
        return SuggestionResult.of("text", "");
    }


}
