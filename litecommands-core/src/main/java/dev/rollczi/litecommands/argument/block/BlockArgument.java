package dev.rollczi.litecommands.argument.block;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.Completion;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.MatchResult;

import java.util.Collections;
import java.util.List;

public class BlockArgument implements Argument<Block> {

    @Override
    public MatchResult match(LiteInvocation invocation, Block annotation, int currentRoute, int currentArgument) {
        if (currentArgument >= invocation.arguments().length) {
            return MatchResult.notMatched();
        }

        if (annotation.value().contains(" ")) {
            String[] blocks = annotation.value().split(" ");

            int i = 0;
            for (String block : blocks) {
                if (!invocation.arguments()[currentArgument + i].equalsIgnoreCase(block)) {
                    return MatchResult.notMatched();
                }

                i++;
            }

            return MatchResult.matched(Collections.emptyList(), blocks.length);
        }

        String arg = invocation.arguments()[currentArgument];

        if (!arg.equalsIgnoreCase(annotation.value())) {
            return MatchResult.notMatched();
        }

        return MatchResult.matched(Collections.emptyList(), 1);
    }

    @Override
    public List<Completion> complete(LiteInvocation invocation, Block annotation) {
        return Collections.singletonList(Completion.multiCompletion(annotation.value().split(" ")));
    }

}
