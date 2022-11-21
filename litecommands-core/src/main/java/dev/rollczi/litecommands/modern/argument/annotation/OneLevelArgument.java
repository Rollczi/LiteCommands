package dev.rollczi.litecommands.modern.argument.annotation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResult;

import java.util.List;

public interface OneLevelArgument<SENDER, TYPE> extends MultiLevelArgument<SENDER, TYPE> {


    @Override
    default ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, AnnotationArgumentContext<Arg, TYPE> contextBox, List<String> arguments) {
        return ArgumentResult.success(parse(invocation, arguments.get(0)), 1);
    }

    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, AnnotationArgumentContext<Arg, TYPE> contextBox, String argument);

    @Override
    default CountRange getArgumentCount() {
        return CountRange.ONE;
    }

}
