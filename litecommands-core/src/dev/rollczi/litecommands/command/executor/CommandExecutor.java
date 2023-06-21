package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.command.requirement.RequirementMatch;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.List;

public interface CommandExecutor<SENDER, REQUIREMENT extends Requirement<SENDER, ?>> extends MetaHolder {

    List<REQUIREMENT> getRequirements();

    CommandExecutorMatchResult match(List<RequirementMatch<SENDER, REQUIREMENT, Object>> results);

}
