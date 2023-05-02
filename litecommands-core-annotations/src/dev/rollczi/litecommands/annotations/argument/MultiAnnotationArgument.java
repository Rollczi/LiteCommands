package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.parser.ArgumentRawInputParser;
import dev.rollczi.litecommands.argument.input.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.ArgumentSuggester;

public interface MultiAnnotationArgument<SENDER, TYPE> extends
    ArgumentSuggester<SENDER, TYPE, ParameterArgument<Arg, TYPE>>,
        ArgumentRawInputParser<SENDER, TYPE> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, Argument<TYPE> argument, RawInput rawInput);

}
