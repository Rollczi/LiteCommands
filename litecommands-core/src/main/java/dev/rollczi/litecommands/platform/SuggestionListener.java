package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.sugesstion.SuggestionStack;

@FunctionalInterface
public interface SuggestionListener<SENDER> {

    SuggestionStack suggest(SENDER sender, LiteInvocation invocation);

}
