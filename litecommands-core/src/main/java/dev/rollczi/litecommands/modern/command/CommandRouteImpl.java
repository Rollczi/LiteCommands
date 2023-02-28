package dev.rollczi.litecommands.modern.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class CommandRouteImpl<SENDER> implements CommandRoute<SENDER> {

    private final String name;
    private final List<String> aliases;
    private final Set<String> namesAndAliases;

    private final List<CommandExecutor<SENDER>> executors = new ArrayList<>();
    private final List<CommandRoute<SENDER>> childRoutes = new ArrayList<>();

    CommandRouteImpl(String name, List<String> aliases) {
        this.name = name;
        this.aliases = Collections.unmodifiableList(new ArrayList<>(aliases));

        Set<String> namesAndAliases = new HashSet<>(this.aliases);
        namesAndAliases.add(this.name);

        this.namesAndAliases = Collections.unmodifiableSet(namesAndAliases);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getAliases() {
        return Collections.unmodifiableList(this.aliases);
    }

    @Override
    public boolean isNameOrAlias(String name) {
        return this.namesAndAliases.contains(name);
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
    public Collection<CommandExecutor<SENDER>> getExecutors() {
        return Collections.unmodifiableCollection(this.executors);
    }

    @Override
    public void appendChildren(CommandRoute<SENDER> children) {
        this.childRoutes.add(children);
    }

    @Override
    public void appendExecutor(CommandExecutor<SENDER> executor) {
        this.executors.add(executor);
    }

}
