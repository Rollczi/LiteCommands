package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.requirement.CommandRequirement;
import dev.rollczi.litecommands.meta.CommandMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandExecutor<SENDER, REQUIREMENT extends CommandRequirement<SENDER, ?>> implements CommandExecutor<SENDER, REQUIREMENT> {

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
    public List<REQUIREMENT> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

}
