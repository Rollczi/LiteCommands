package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.FailedReason;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface RequirementCondition {

    Optional<FailedReason> check(Invocation<?> invocation, CommandExecutorMatchResult result);

}
