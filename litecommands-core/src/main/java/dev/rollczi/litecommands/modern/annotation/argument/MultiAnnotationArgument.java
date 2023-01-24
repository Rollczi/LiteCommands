package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.argument.ArgumentResolver;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResolver;

import java.util.List;

public interface MultiAnnotationArgument<SENDER, TYPE> extends
    ArgumentResolver<SENDER, Arg, TYPE, ParameterArgumentContextual<Arg, TYPE>>,
    SuggestionResolver<SENDER, Arg, TYPE, ParameterArgumentContextual<Arg, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ParameterArgumentContextual<Arg, TYPE> context);

}
