package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.Requirement;

import java.util.List;

@FunctionalInterface
public interface CommandExecutorFactory<SENDER> {

    CommandExecutor<SENDER> create(CommandRoute<SENDER> parent, List<Requirement<SENDER, ?>> requirements);

}
