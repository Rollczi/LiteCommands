package dev.rollczi.litecommands.argument.block;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentContext;
import dev.rollczi.litecommands.sugesstion.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;
import panda.std.Blank;
import panda.std.Option;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

public class BlockArgument<SENDER> implements Argument<SENDER, Block> {

    @Override
    public MatchResult match(LiteInvocation invocation, ArgumentContext<Block> context) {
        String[] blocks = context.annotation().value().split(" ");
        int currentArgument = context.currentArgument();

        if (currentArgument + blocks.length > invocation.arguments().length) {
            return MatchResult.notMatched();
        }

        int i = 0;
        for (String block : blocks) {
            if (!invocation.arguments()[currentArgument + i].equalsIgnoreCase(block)) {
                return MatchResult.notMatched();
            }

            i++;
        }

        if (context.parameter().getType().equals(Blank.class)) {
            return MatchResult.matched(Collections.singletonList(Blank.BLANK), blocks.length);
        }

        return MatchResult.matched(Collections.emptyList(), blocks.length);
    }

    @Override
    public List<Suggestion> suggestion(LiteInvocation invocation, Parameter parameter, Block annotation) {
        return Collections.singletonList(Suggestion.multilevel(annotation.value().split(" ")));
    }

    @Override
    public Option<String> getSchematic(Block annotation) {
        return Option.of(annotation.value());
    }

    @Override
    public boolean canHandle(Class<?> type, Parameter parameter) {
        return true;
    }

    @Override
    public boolean canHandleAssignableFrom(Class<?> type, Parameter parameter) {
        return true;
    }

}
