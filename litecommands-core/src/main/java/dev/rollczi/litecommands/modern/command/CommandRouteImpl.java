package dev.rollczi.litecommands.modern.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandRouteImpl implements CommandRoute {

    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private final Set<String> namesAndAliasesLowerCase = new HashSet<>();

    private final Map<CommandExecutorKey, CommandExecutor> executors = new LinkedHashMap<>();
    private final List<CommandRoute> childRoutes = new ArrayList<>();

    public CommandRouteImpl(String name, List<String> aliases) {
        this.name = name;
        this.aliases.addAll(aliases);

        this.namesAndAliasesLowerCase.add(name.toLowerCase(Locale.ROOT));
        this.namesAndAliasesLowerCase.addAll(aliases.stream()
            .map(alias -> alias.toLowerCase(Locale.ROOT))
            .collect(Collectors.toList()));
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
        return this.namesAndAliasesLowerCase.contains(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public List<CommandRoute> getChildren() {
        return Collections.unmodifiableList(this.childRoutes);
    }

    @Override
    public Collection<CommandExecutor> getExecutors() {
        return Collections.unmodifiableCollection(this.executors.values());
    }

    @Override
    public Optional<CommandExecutor> getExecutor(CommandExecutorKey key) {
        return Optional.ofNullable(this.executors.get(key));
    }

    @Override
    public void appendChildren(CommandRoute children) {
        this.childRoutes.add(children);
    }

    @Override
    public void appendExecutor(CommandExecutor executor) {
        this.executors.put(executor.getKey(), executor);
    }

}
