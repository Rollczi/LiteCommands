package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.priority.PriorityLevel;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class CommandExecutorDefaultReference<SENDER> implements CommandExecutor<SENDER> {

    private final CommandExecutor<SENDER> executor;

    public CommandExecutorDefaultReference(CommandExecutor<SENDER> executor) {
        if (!executor.getArguments().isEmpty()) {
            throw new IllegalArgumentException("Default executor cannot have any arguments");
        }

        this.executor = executor;
    }

    @Override
    public @Unmodifiable List<Argument<?>> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public @Unmodifiable List<ContextRequirement<?>> getContextRequirements() {
        return executor.getContextRequirements();
    }

    @Override
    public @Unmodifiable List<BindRequirement<?>> getBindRequirements() {
        return executor.getBindRequirements();
    }

    @Override
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> result) {
        return executor.match(result);
    }

    @Override
    public CommandRoute<SENDER> getParent() {
        return executor.getParent();
    }

    @Override
    public Meta meta() {
        return executor.meta();
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return executor.parentMeta();
    }

    @Override
    public PriorityLevel getPriority() {
        return PriorityLevel.NONE;
    }

}
