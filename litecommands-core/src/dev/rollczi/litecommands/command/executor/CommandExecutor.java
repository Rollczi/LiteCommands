package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.CommandNode;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.scope.Scopeable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface CommandExecutor<SENDER> extends Scopeable, CommandNode<SENDER> {

    List<Argument<?>> getArguments();

    List<ContextRequirement<?>> getContextRequirements();

    List<BindRequirement<?>> getBindRequirements();

    CommandExecutorMatchResult match(RequirementsResult<SENDER> result);

    @Override
    default Collection<String> names() {
        return Collections.emptySet();
    }

    static <SENDER> CommandExecutorBuilder<SENDER> builder(CommandRoute<SENDER> parent) {
        return new CommandExecutorBuilder<>(parent);
    }
}
