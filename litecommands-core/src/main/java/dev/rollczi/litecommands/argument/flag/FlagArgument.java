package dev.rollczi.litecommands.argument.flag;

import dev.rollczi.litecommands.argument.SingleArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

public class FlagArgument implements SingleArgument<Flag> {

    @Override
    public MatchResult match(LiteInvocation invocation, Flag annotation, int currentRoute, int currentArgument, String argument) {
        if (annotation.value().equals(argument)) {
            return MatchResult.matched(true, 1);
        }

        return MatchResult.matched(false, 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }

}
