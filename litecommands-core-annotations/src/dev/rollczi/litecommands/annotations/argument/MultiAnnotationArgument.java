package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.argument.ArgumentParser;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.ArgumentSuggester;

import java.util.List;

public interface MultiAnnotationArgument<SENDER, TYPE> extends
    ArgumentParser<SENDER, TYPE, ParameterArgument<Arg, TYPE>>,
    ArgumentSuggester<SENDER, TYPE, ParameterArgument<Arg, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, ParameterArgument<Arg, TYPE> argument, List<String> arguments);

}
