package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.CommandNode;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.priority.Prioritized;
import dev.rollczi.litecommands.bind.BindRequirement;
import dev.rollczi.litecommands.context.ContextRequirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.scope.Scopeable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Unmodifiable;

/**
 * CommandExecutor is a node of the command tree.
 * Argument, ContextRequirement and BindRequirement are used to match the command.
 * @see CommandExecutor#match(RequirementsResult)
 */
public interface CommandExecutor<SENDER> extends Scopeable, CommandNode<SENDER>, Prioritized {

    @Unmodifiable
    List<Argument<?>> getArguments();

    @Unmodifiable
    List<ContextRequirement<?>> getContextRequirements();

    @Unmodifiable
    List<BindRequirement<?>> getBindRequirements();

    CommandExecutorMatchResult match(RequirementsResult<SENDER> result);

    @Unmodifiable
    @Override
    default Collection<String> names() {
        return Collections.emptySet();
    }

    static <SENDER> CommandExecutorBuilder<SENDER> builder(CommandRoute<SENDER> parent) {
        return new CommandExecutorBuilder<>(parent);
    }

}
