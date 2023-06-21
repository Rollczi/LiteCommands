package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.MetaCollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

class CommandRouteImpl<SENDER> implements CommandRoute<SENDER> {

    private final String name;
    private final UUID uniqueId = UUID.randomUUID();
    private final List<String> aliases;
    private final Set<String> namesAndAliases;
    private final CommandRoute<SENDER> parent;
    private final CommandMeta meta = CommandMeta.create();

    private final List<CommandExecutor<SENDER, ?>> executors = new ArrayList<>();
    private final List<CommandRoute<SENDER>> childRoutes = new ArrayList<>();

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
    public Optional<CommandRoute<SENDER>> getChildren(String name) {
        return this.childRoutes.stream()
            .filter(route -> route.isNameOrAlias(name))
            .findFirst();
    }

    @Override
    public List<CommandExecutor<SENDER, ?>> getExecutors() {
        return Collections.unmodifiableList(this.executors);
    }

    @Override
    public CommandMeta meta() {
        return this.meta;
    }

    @Override
    public MetaCollector metaCollector() {
        return new CommandRouteMetaCollector(this);
    }

    @Override
    public void appendChildren(CommandRoute<SENDER> children) {
        this.childRoutes.add(children);
    }

    @Override
    public void appendExecutor(CommandExecutor<SENDER, ?> executor) {
        this.executors.add(executor);
    }

}
