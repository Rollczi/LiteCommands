package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.SuggestionStack;

@FunctionalInterface
public interface SuggestionListener<SENDER> {

    SuggestionStack suggest(SENDER sender, LiteInvocation invocation);

}
