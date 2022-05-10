package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.SuggestResult;
import dev.rollczi.litecommands.command.LiteInvocation;

@FunctionalInterface
public interface Suggester<SENDER> {

    SuggestResult suggest(SENDER sender, LiteInvocation invocation);

}
