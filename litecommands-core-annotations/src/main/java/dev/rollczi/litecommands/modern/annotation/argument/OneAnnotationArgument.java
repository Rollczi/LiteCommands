package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.range.Range;

import java.util.List;

public abstract class OneAnnotationArgument<SENDER, TYPE> implements MultiAnnotationArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, ParameterArgument<Arg, TYPE> argument, List<String> arguments) {
        return this.parse(invocation, arguments.get(0), argument);
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, String argument, ParameterArgument<Arg, TYPE> context);

    @Override
    public final Range getRange() {
        return Range.ONE;
    }

}
