package dev.rollczi.litecommands.modern.core.argument;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.api.OneArgument;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.List;

public class StringArgument<SENDER> extends OneArgument<SENDER, String> {

    @Override
    protected ArgumentResult<String> parse(Invocation<SENDER> invocation, Argument<Object, String> context, String argument) {
        return ArgumentResult.success(() -> argument);
    }

    @Override
    public List<Suggestion> suggestion(Invocation<SENDER> invocation, Argument<Object, String> objectStringArgument, Suggestion suggestion) {
        return Suggestion.of("text", "");

    }
}
