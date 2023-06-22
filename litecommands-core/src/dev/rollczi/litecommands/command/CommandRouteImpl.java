package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaCollector;
import dev.rollczi.litecommands.meta.MetaHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

class CommandRouteImpl<SENDER> implements CommandRoute<SENDER> {

    private final String name;
    private final UUID uniqueId = UUID.randomUUID();
    private final List<String> aliases;
    private final Set<String> namesAndAliases;
    private final CommandRoute<SENDER> parent;
    private final Meta meta = Meta.create();
    private final MetaCollector metaCollector = MetaCollector.of(this);

    private final List<CommandExecutor<SENDER, ?>> executors = new ArrayList<>();
    private final List<CommandRoute<SENDER>> childRoutes = new ArrayList<>();
    private final Map<String, CommandRoute<SENDER>> childrenByName = new HashMap<>();

    CommandRouteImpl(String name, List<String> aliases, CommandRoute<SENDER> parent) {
        this.name = name;
        this.aliases = Collections.unmodifiableList(new ArrayList<>(aliases));
        this.parent = parent;

        Set<String> namesAndAliases = new HashSet<>(this.aliases);
        namesAndAliases.add(this.name);

        this.namesAndAliases = Collections.unmodifiableSet(namesAndAliases);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public List<String> names() {
        return Collections.unmodifiableList(new ArrayList<>(this.namesAndAliases));
    }

    @Override
    public boolean isNameOrAlias(String name) {
        return this.namesAndAliases.contains(name);
    }

    @Override
    public CommandRoute<SENDER> getParent() {
        return parent;
    }

    @Override
    public List<CommandRoute<SENDER>> getChildren() {
        return Collections.unmodifiableList(this.childRoutes);
    }

    @Override
    public Optional<CommandRoute<SENDER>> getChild(String name) {
        return Optional.ofNullable(this.childrenByName.get(name));
    }

    @Override
    public List<CommandExecutor<SENDER, ?>> getExecutors() {
        return Collections.unmodifiableList(this.executors);
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return parent;
    }

    @Override
    public MetaCollector metaCollector() {
        return this.metaCollector;
    }

    @Override
    public void appendChildren(CommandRoute<SENDER> children) {
        for (String name : children.names()) {
            if (this.childrenByName.containsKey(name)) {
                throw new IllegalArgumentException("Route with name or alias '" + name + "' already exists!");
            }
        }

        this.childRoutes.add(children);
        for (String name : children.names()) {
            this.childrenByName.put(name, children);
        }
    }

    @Override
    public void appendExecutor(CommandExecutor<SENDER, ?> executor) {
        this.executors.add(executor);
    }

}
