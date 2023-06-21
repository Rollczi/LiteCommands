package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.command.requirement.RequirementMatch;
import dev.rollczi.litecommands.meta.Meta;

import java.util.List;

public interface CommandExecutor<SENDER, REQUIREMENT extends Requirement<SENDER, ?>> {

    List<REQUIREMENT> getRequirements();

    Meta getMeta();

    CommandExecutorMatchResult match(List<RequirementMatch<SENDER, REQUIREMENT, Object>> results);

}
