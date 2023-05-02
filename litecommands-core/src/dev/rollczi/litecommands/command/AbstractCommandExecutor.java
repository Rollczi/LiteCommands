package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.requirements.CommandRequirement;
import dev.rollczi.litecommands.command.requirements.CommandRequirementResult;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.argument.input.InputArgumentsMatcher;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandExecutor<SENDER, REQUIREMENT extends CommandRequirement<SENDER, ?>> implements CommandExecutor<SENDER> {

    protected final List<REQUIREMENT> requirements = new ArrayList<>();
    protected final CommandMeta meta = CommandMeta.create();

    protected AbstractCommandExecutor(Collection<? extends REQUIREMENT> requirements) {
        this.requirements.addAll(requirements);
    }

    @Override
    public CommandMeta getMeta() {
        return meta;
    }

    @Override
    public List<CommandRequirement<SENDER, ?>> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    @Override
    public <MATCHER extends InputArgumentsMatcher<MATCHER>> CommandExecutorMatchResult match(Invocation<SENDER> invocation, InputArguments<MATCHER> inputArguments, MATCHER matcher) {
        List<Match<REQUIREMENT>> results = new ArrayList<>();

        for (REQUIREMENT requirement : requirements) {
            CommandRequirementResult<?> result = requirement.check(invocation, inputArguments, matcher);

            if (!result.isSuccess()) {
                return CommandExecutorMatchResult.failed(result.getFailedReason());
            }

            results.add(new Match<>(requirement, result));
        }

        InputArgumentsMatcher.EndResult endResult = matcher.endMatch();

        if (!endResult.isSuccessful()) {
            return CommandExecutorMatchResult.failed(endResult.getFailedReason());
        }

        return match(results);
    }

    protected abstract CommandExecutorMatchResult match(List<Match<REQUIREMENT>> results);

    protected static class Match<ARGUMENT> {
        private final ARGUMENT argument;
        private final CommandRequirementResult<?> result;

        private Match(ARGUMENT argument, CommandRequirementResult<?> result) {
            this.argument = argument;
            this.result = result;
        }

        public ARGUMENT getArgument() {
            return argument;
        }

        public CommandRequirementResult<?> getResult() {
            return result;
        }
    }

}
