package dev.rollczi.litecommands.modern.extension.annotation;

import dev.rollczi.litecommands.modern.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.extension.annotation.method.AnnotatedParameterArgumentContextual;

import java.util.List;

public abstract class OneAnnotationArgument<SENDER, TYPE> implements MultiAnnotationArgument<SENDER, TYPE> {

    @Override
    public final ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, List<String> arguments, AnnotatedParameterArgumentContextual<Arg, TYPE> context) {
        return parse(invocation, arguments.get(0), context);
    }

    protected abstract ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, String argument, AnnotatedParameterArgumentContextual<Arg, TYPE> context);

    @Override
    public final CountRange getRange() {
        return CountRange.ONE;
    }

}
