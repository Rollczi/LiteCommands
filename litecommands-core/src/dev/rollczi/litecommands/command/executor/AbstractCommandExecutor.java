package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandExecutor<SENDER, REQUIREMENT extends Requirement<SENDER, ?>> implements CommandExecutor<SENDER, REQUIREMENT> {

    protected final CommandRoute<SENDER> parent;
    protected final List<REQUIREMENT> requirements = new ArrayList<>();
    protected final Meta meta = Meta.create();

    protected AbstractCommandExecutor(CommandRoute<SENDER> parent, Collection<? extends REQUIREMENT> requirements) {
        this.parent = parent;
        this.requirements.addAll(requirements);
    }

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public MetaHolder parentMeta() {
        return parent;
    }

    @Override
    public CommandRoute<SENDER> getParent() {
        return parent;
    }

    @Override
    public List<REQUIREMENT> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

}
