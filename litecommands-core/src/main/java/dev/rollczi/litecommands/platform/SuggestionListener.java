package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.SuggestResult;

@FunctionalInterface
public interface SuggestionListener<SENDER> {

    SuggestResult suggest(SENDER sender, LiteInvocation invocation);

}
