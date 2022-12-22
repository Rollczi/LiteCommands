package dev.rollczi.litecommands.modern.command.argument.method;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentInvocationParserInternal;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;

import java.util.List;

public interface MultiArgument<SENDER, TYPE> extends ArgumentInvocationParserInternal<SENDER, Arg, TYPE, AnnotatedParameterArgumentContext<Arg, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, AnnotatedParameterArgumentContext<Arg, TYPE> context, List<String> arguments);

    @Override
    CountRange getArgumentsRange();

}
