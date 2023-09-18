package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecuteResult;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.executor.CommandExecutorMatchResult;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.requirement.RequirementsResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

class ProgrammaticCommandExecutor<SENDER> implements CommandExecutor<SENDER> {

    private final CommandRoute<SENDER> parent;
    private final List<Requirement<SENDER, ?>> requirements;
    private final LiteCommand<SENDER> liteCommand;
    private final Meta meta = Meta.create();

    public ProgrammaticCommandExecutor(CommandRoute<SENDER> parent, List<Requirement<SENDER, ?>> requirements, LiteCommand<SENDER> liteCommand) {
        this.parent = parent;
        this.requirements = requirements;
        this.liteCommand = liteCommand;
    }

    @Override
    public CommandRoute<SENDER> getParent() {
        return this.parent;
    }

    @Override
    public List<Requirement<SENDER, ?>> getRequirements() {
        return Collections.unmodifiableList(this.requirements);
    }

    @Override
    public CommandExecutorMatchResult match(RequirementsResult<SENDER> result) {
        for (String requirement : liteCommand.getRequirements()) {
            if (!result.has(requirement)) {
                return CommandExecutorMatchResult.failed(new IllegalStateException("Missing requirement " + requirement));
            }
        }

        return CommandExecutorMatchResult.success(() -> {
            liteCommand.execute(new LiteContext<>(result));
            return CommandExecuteResult.success(this, null);
        });
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return this.parent;
    }

}
