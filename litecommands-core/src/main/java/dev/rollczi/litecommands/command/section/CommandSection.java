package dev.rollczi.litecommands.command.section;

import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionMerger;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.suggestion.Suggester;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.List;
import java.util.Set;

public interface CommandSection<SENDER> extends Suggester, MetaHolder {

    String getName();

    Set<String> getAliases();

    boolean isSimilar(String name);

    ExecuteResult execute(Invocation<SENDER> invocation);

    SuggestionMerger findSuggestion(Invocation<SENDER> invocation, int route);

    FindResult<SENDER> find(LiteInvocation invocation, int route, FindResult<SENDER> lastResult);

    /**
     * Command section
     */

    void childSection(CommandSection<SENDER> commandSection);

    void mergeSection(CommandSection<SENDER> section);

    List<CommandSection<SENDER>> childrenSection();

    /**
     * Argument executor
     */

    void executor(ArgumentExecutor<SENDER> argumentExecutor);

    List<ArgumentExecutor<SENDER>> executors();

    @Override
    CommandMeta meta();

}
