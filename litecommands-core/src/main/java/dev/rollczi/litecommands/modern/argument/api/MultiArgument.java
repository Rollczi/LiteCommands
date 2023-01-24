package dev.rollczi.litecommands.modern.argument.api;

import dev.rollczi.litecommands.modern.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.argument.ArgumentResolver;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResolver;

import java.util.List;

public interface MultiArgument<SENDER, TYPE> extends
    ArgumentResolver<SENDER, Object, TYPE, ArgumentContextual<Object, TYPE>>,
    SuggestionResolver<SENDER, Object, TYPE, ArgumentContextual<Object, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ArgumentContextual<Object, TYPE> context);

}
