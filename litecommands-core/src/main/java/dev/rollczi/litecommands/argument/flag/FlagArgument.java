package dev.rollczi.litecommands.argument.flag;

import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.argument.SingleArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.reflect.Parameter;

public class FlagArgument<SENDER> implements SingleArgument<SENDER, Flag> {

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Flag> context, String argument) {
        if (context.annotation().value().equals(argument)) {
            return MatchResult.matched(true, 1);
        }

        return MatchResult.matched(false, 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }

}
