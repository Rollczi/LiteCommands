package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.command.requirement.Requirement;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaCollector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractCommandExecutor<SENDER, REQUIREMENT extends Requirement<SENDER, ?>> implements CommandExecutor<SENDER, REQUIREMENT> {

    protected final List<REQUIREMENT> requirements = new ArrayList<>();
    protected final Meta meta = Meta.create();

    protected AbstractCommandExecutor(Collection<? extends REQUIREMENT> requirements) {
        this.requirements.addAll(requirements);
    }

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public void editMeta(Consumer<Meta> operator) {
        operator.accept(meta);
    }

    @Override
    public MetaCollector metaCollector() {
        return MetaCollector.of(meta);
    }

    @Override
    public List<REQUIREMENT> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

}
