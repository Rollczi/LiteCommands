package dev.rollczi.litecommands.argument.type.baisc;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.type.OneArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

public class StringArgumentResolver<SENDER> extends OneArgumentResolver<SENDER, String> {

    @Override
    protected ArgumentResult<String> parse(Invocation<SENDER> invocation, Argument<String> context, String argument) {
        return ArgumentResult.success(() -> argument);
    }

    @Override
    protected boolean canParse(Invocation<SENDER> invocation, Argument<String> context, String argument) {
        return true;
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> objectStringArgument, SuggestionContext suggestion) {
        return SuggestionResult.of("text", "");
    }


}
