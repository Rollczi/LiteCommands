package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.ArgumentSuggester;

import java.util.List;

public interface MultiAnnotationArgument<SENDER, TYPE> extends
    ArgumentParser<SENDER, TYPE, ParameterArgument<Arg, TYPE>>,
    ArgumentSuggester<SENDER, TYPE, ParameterArgument<Arg, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, ParameterArgument<Arg, TYPE> argument, List<String> arguments);

}
