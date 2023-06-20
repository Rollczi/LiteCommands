package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.command.requirement.RequirementSuccessMatch;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.List;

public interface CommandExecutor<SENDER, REQUIREMENT extends Requirement<SENDER, ?>> {

    List<REQUIREMENT> getRequirements();

    CommandMeta getMeta();

    CommandExecutorMatchResult match(List<RequirementSuccessMatch<SENDER, REQUIREMENT, Object>> results);

}
