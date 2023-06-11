package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.argument.input.ArgumentsInputMatcher;
import dev.rollczi.litecommands.command.requirements.CommandRequirement;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.List;

public interface CommandExecutor<SENDER> {

    List<CommandRequirement<SENDER, ?>> getRequirements();

    CommandMeta getMeta();

    <MATCHER extends ArgumentsInputMatcher<MATCHER>> CommandExecutorMatchResult match(Invocation<SENDER> invocation, MATCHER matcher);

}
