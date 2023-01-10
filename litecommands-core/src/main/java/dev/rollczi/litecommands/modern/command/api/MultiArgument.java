package dev.rollczi.litecommands.modern.command.api;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.suggestion.SuggestionResolver;

import java.util.List;

public interface MultiArgument<SENDER, TYPE> extends
    ArgumentResolver<SENDER, Object, TYPE, ArgumentContext<Object, TYPE>>,
    SuggestionResolver<SENDER, Object, TYPE, ArgumentContext<Object, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ArgumentContext<Object, TYPE> context);

}
