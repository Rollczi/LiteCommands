package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandExecutor<SENDER> implements CommandExecutor<SENDER> {

    protected final CommandRoute<SENDER> parent;
    protected final List<Requirement<SENDER, ?>> requirements = new ArrayList<>();
    protected final Meta meta = Meta.create();

    protected AbstractCommandExecutor(CommandRoute<SENDER> parent, Collection<? extends Requirement<SENDER, ?>> requirements) {
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
    public List<Requirement<SENDER, ?>> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

}
