package dev.rollczi.litecommands.argument.joiner;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.util.Arrays;
import java.util.List;

public class JoinerArgument implements Argument<Joiner> {

    @Override
    public MatchResult match(LiteInvocation invocation, Joiner annotation, int currentRoute, int currentArgument) {
        if (invocation.arguments().length < currentArgument) {
            return MatchResult.notMatched();
        }

        List<String> args = Arrays.asList(invocation.arguments());
        List<String> toJoin = args.subList(currentArgument, args.size());

        if (toJoin.isEmpty()) {
            return MatchResult.notMatched();
        }

        return MatchResult.matched(String.join(" ", toJoin), toJoin.size());
    }

}
