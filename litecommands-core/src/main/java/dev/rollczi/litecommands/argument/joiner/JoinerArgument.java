package dev.rollczi.litecommands.argument.joiner;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class JoinerArgument<SENDER> implements Argument<SENDER, Joiner> {

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Joiner> context) {
        int currentArgument = context.currentArgument();

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
