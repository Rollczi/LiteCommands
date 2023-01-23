package dev.rollczi.litecommands.modern.extension.annotation.command.argument;

import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;

import java.util.List;

public abstract class OneAnnotationArgument<SENDER, TYPE> implements MultiAnnotationArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, ParameterArgumentContextual<Arg, TYPE> context) {
        return this.parse(invocation, arguments.get(0), context);
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, String argument, ParameterArgumentContextual<Arg, TYPE> context);

    @Override
    public final CountRange getRange() {
        return CountRange.ONE;
    }

}
