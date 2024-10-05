package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaCollector;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.priority.MutablePrioritizedList;
import dev.rollczi.litecommands.priority.PrioritizedList;
import dev.rollczi.litecommands.shared.Preconditions;
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

    private final MutablePrioritizedList<CommandExecutor<SENDER>> executors = new MutablePrioritizedList<>();
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
    public PrioritizedList<CommandExecutor<SENDER>> getExecutors() {
        return this.executors;
    }

    @Override
    public Meta meta() {
        return this.meta;
    }

    @Override
    public CommandRoute<SENDER> merge(CommandRoute<SENDER> toMerge) {
        if (!this.name.equals(toMerge.getName())) {
            throw new IllegalArgumentException("Cannot merge routes with different names!");
        }

        List<String> aliases = new ArrayList<>(this.aliases);
        aliases.addAll(toMerge.getAliases());

        CommandRouteImpl<SENDER> merged = new CommandRouteImpl<SENDER>(this.name, aliases, this.parent) {
            @Override
            public boolean isReference() {
                return CommandRouteImpl.this.isReference(); // because parent is the same. TODO: refactor
            }
        };

        merged.meta().putAll(this.meta());
        merged.meta().putAll(toMerge.meta());

        for (CommandExecutor<SENDER> executor : this.getExecutors()) {
            merged.appendExecutor(executor);
        }

        for (CommandExecutor<SENDER> executor : toMerge.getExecutors()) {
            merged.appendExecutor(executor);
        }

        for (CommandRoute<SENDER> child : this.getChildren()) {
            merged.appendChildren(child);
        }

        for (CommandRoute<SENDER> child : toMerge.getChildren()) {
            merged.appendChildren(child);
        }

        return merged;
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
    public void appendExecutor(CommandExecutor<SENDER> executor) {
        Preconditions.notNull(executor, "executor");
        Preconditions.notContains(this.executors, executor, "executors", "executor");
        this.executors.add(executor);
    }

    @Override
    public String toString() {
        return "CommandRouteImpl{" +
            "name='" + name + '\'' +
            ", uniqueId=" + uniqueId +
            ", aliases=" + aliases +
            ", meta=" + meta +
            '}';
    }
}
