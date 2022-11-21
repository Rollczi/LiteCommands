package dev.rollczi.litecommands.modern.argument.annotation;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.modern.argument.invocation.ArgumentResult;

import java.util.List;

public class SimpleOneArgument implements OneLevelArgument<Void, String> {

    @Override
    public ArgumentResult<String> parse(Invocation<Void> invocation, AnnotationArgumentContext<Arg, String> contextBox, List<String> arguments) {
        return ArgumentResult.success(arguments.get(0));
    }

}
