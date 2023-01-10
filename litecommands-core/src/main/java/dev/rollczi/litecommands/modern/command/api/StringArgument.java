package dev.rollczi.litecommands.modern.command.api;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.util.List;

public class StringArgument<SENDER> extends OneArgument<SENDER, String> {

    @Override
    protected ArgumentResult<String> parse(Invocation<SENDER> invocation, ArgumentContext<Object, String> context, String argument) {
        return ArgumentResult.success(() -> argument);
    }

    @Override
    public List<Suggestion> suggestion(Invocation<SENDER> invocation, ArgumentContext<Object, String> context) {
        return Suggestion.of("text", "");
    }

}
