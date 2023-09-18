package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.CommandNode;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.scope.Scopeable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface CommandExecutor<SENDER> extends Scopeable, CommandNode<SENDER> {

    @Deprecated
    List<Requirement<SENDER, ?>> getRequirements();

    CommandExecutorMatchResult match(RequirementsResult<SENDER> result);

    @Override
    default Collection<String> names() {
        return Collections.emptySet();
    }

}
