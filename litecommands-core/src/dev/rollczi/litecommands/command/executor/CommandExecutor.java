package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.CommandNode;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import dev.rollczi.litecommands.scope.Scopeable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface CommandExecutor<SENDER, REQUIREMENT extends Requirement<SENDER, ?>> extends Scopeable, CommandNode<SENDER> {

    List<REQUIREMENT> getRequirements();

    CommandExecutorMatchResult match(RequirementsResult<SENDER> result);

    @Override
    default Collection<String> names() {
        return Collections.emptySet();
    }

}
