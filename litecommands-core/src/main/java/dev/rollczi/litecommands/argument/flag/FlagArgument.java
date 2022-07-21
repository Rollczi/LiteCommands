package dev.rollczi.litecommands.argument.flag;

import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.argument.SingleOrElseArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

public class FlagArgument<SENDER> implements SingleOrElseArgument<SENDER, Flag> {

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Flag> context, String argument) {
        if (context.annotation().value().equals(argument)) {
            return MatchResult.matched(true, 1);
        }

        return MatchResult.matched(false, 0);
    }

    @Override
    public MatchResult orElse(LiteInvocation invocation, ArgumentContext<Flag> context) {
        return MatchResult.matched(false, 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }

}
