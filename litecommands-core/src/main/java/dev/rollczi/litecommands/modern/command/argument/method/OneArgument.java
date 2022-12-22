package dev.rollczi.litecommands.modern.command.argument.method;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;

import java.util.List;

public abstract class OneArgument<SENDER, TYPE> implements MultiArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, AnnotatedParameterArgumentContext<Arg, TYPE> context, List<String> arguments) {
        return parse(invocation, context, arguments.get(0));
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, AnnotatedParameterArgumentContext<Arg, TYPE> contextBox, String argument);

    @Override
    public final CountRange getArgumentsRange() {
        return CountRange.ONE;
    }

}
