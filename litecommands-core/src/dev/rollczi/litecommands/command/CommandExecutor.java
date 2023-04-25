package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.command.requirements.CommandRequirement;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.List;

public interface CommandExecutor<SENDER> {

    List<CommandRequirement<SENDER, ?>> getRequirements();

    CommandMeta getMeta();

    <CONTEXT extends InputArgumentsMatcher<CONTEXT>> CommandExecutorMatchResult match(Invocation<SENDER> invocation, InputArguments<CONTEXT> inputArguments, CONTEXT context);

}
