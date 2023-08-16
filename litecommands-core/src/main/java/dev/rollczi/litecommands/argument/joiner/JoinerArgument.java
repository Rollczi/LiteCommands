package dev.rollczi.litecommands.argument.joiner;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class JoinerArgument<SENDER, RESULT> implements Argument<SENDER, Joiner> {

    private final Function<String, RESULT> resultMapper;

    protected JoinerArgument(Function<String, RESULT> resultMapper) {
        this.resultMapper = resultMapper;
    }

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

        return MatchResult.matched(resultMapper.apply(String.join(joiner.delimiter(), toJoin)), toCut);
    }

}
