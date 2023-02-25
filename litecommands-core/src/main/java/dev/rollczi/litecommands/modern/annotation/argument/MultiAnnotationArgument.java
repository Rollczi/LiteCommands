package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.argument.ArgumentResolver;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResolver;

import java.util.List;

public interface MultiAnnotationArgument<SENDER, TYPE> extends
    ArgumentResolver<SENDER, Arg, TYPE, ParameterArgument<Arg, TYPE>>,
    SuggestionResolver<SENDER, Arg, TYPE, ParameterArgument<Arg, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ParameterArgument<Arg, TYPE> context);

}
