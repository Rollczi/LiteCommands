package dev.rollczi.litecommands.modern.argument.annotation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.count.CountRange;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentParser;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResult;

import java.util.List;

public interface MultiLevelArgument<SENDER, TYPE> extends ArgumentParser<SENDER, Arg, TYPE, AnnotationArgumentContext<Arg, TYPE>> {

    @Override
    ArgumentResult<TYPE> parse(Invocation<SENDER> invocation, AnnotationArgumentContext<Arg, TYPE> contextBox, List<String> arguments);

    @Override
    CountRange getArgumentCount();

}
