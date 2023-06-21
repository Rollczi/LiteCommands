package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.meta.Meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandExecutor<SENDER, REQUIREMENT extends Requirement<SENDER, ?>> implements CommandExecutor<SENDER, REQUIREMENT> {

    protected final List<REQUIREMENT> requirements = new ArrayList<>();
    protected final Meta meta = Meta.create();

    protected AbstractCommandExecutor(Collection<? extends REQUIREMENT> requirements) {
        this.requirements.addAll(requirements);
    }

    @Override
    public Meta getMeta() {
        return meta;
    }

    @Override
    public List<REQUIREMENT> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

}
