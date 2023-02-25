package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;

import java.util.List;

public abstract class OneAnnotationArgument<SENDER, TYPE> implements MultiAnnotationArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ParameterArgument<Arg, TYPE> context) {
        return this.parse(invocation, arguments.get(0), context);
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, String argument, ParameterArgument<Arg, TYPE> context);

    @Override
    public final CountRange getRange() {
        return CountRange.ONE;
    }

}
