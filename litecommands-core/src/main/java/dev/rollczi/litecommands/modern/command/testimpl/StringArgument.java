package dev.rollczi.litecommands.modern.command.testimpl;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.modern.command.argument.method.AnnotatedParameterArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.method.Arg;
import dev.rollczi.litecommands.modern.command.argument.method.OneArgument;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;

public class StringArgument extends OneArgument<Void, String> {

    @Override
    public ArgumentResult<String> parse(Invocation<Void> invocation, AnnotatedParameterArgumentContext<Arg, String> contextBox, String argument) {
        return ArgumentResult.success(() -> argument);
    }

}
