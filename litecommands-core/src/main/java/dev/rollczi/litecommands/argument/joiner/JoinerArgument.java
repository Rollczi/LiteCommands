package dev.rollczi.litecommands.argument.joiner;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.util.Arrays;
import java.util.List;

public class JoinerArgument<SENDER> implements Argument<SENDER, Joiner> {

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Joiner> context) {
        int currentArgument = context.currentArgument();

        if (invocation.arguments().length < currentArgument) {
            return MatchResult.notMatched();
        }

        Joiner joiner = context.annotation();

        List<String> args = Arrays.asList(invocation.arguments());
        int toCut =  joiner.limit() == - 1 ? args.size() : Math.min(args.size(), joiner.limit() + currentArgument);
        List<String> toJoin = args.subList(currentArgument, toCut);

        if (toJoin.isEmpty()) {
            return MatchResult.notMatched();
        }

        return MatchResult.matched(String.join(joiner.delimiter(), toJoin), toCut);
    }

}
