package dev.rollczi.litecommands.modern.extension.annotation;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.suggestion.SuggestionResolver;
import dev.rollczi.litecommands.modern.extension.annotation.method.AnnotatedParameterArgumentContext;

import java.util.List;

public interface MultiAnnotationArgument<SENDER, TYPE> extends
    ArgumentResolver<SENDER, Arg, TYPE, AnnotatedParameterArgumentContext<Arg, TYPE>>,
    SuggestionResolver<SENDER, Arg, TYPE, AnnotatedParameterArgumentContext<Arg, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, AnnotatedParameterArgumentContext<Arg, TYPE> context);

}
