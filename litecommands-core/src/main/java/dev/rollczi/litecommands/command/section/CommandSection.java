package dev.rollczi.litecommands.command.section;

import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.meta.Meta;
import dev.rollczi.litecommands.command.sugesstion.Suggester;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.sugesstion.SuggestionStack;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;

import java.util.List;
import java.util.Set;

public interface CommandSection extends Suggester {

    String getName();

    Set<String> getAliases();

    boolean isSimilar(String name);

    ExecuteResult execute(LiteInvocation invocation);

    SuggestionStack suggestion(LiteInvocation invocation);

    FindResult find(LiteInvocation invocation, int route, FindResult lastResult);

    /**
     * Command section
     */

    void childSection(CommandSection commandSection);

    void mergeSection(CommandSection section);

    List<CommandSection> childrenSection();

    /**
     * Argument executor
     */

    void executor(ArgumentExecutor argumentExecutor);

    List<ArgumentExecutor> executors();

    Meta meta();

}
