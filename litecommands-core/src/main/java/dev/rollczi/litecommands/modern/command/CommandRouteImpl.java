package dev.rollczi.litecommands.modern.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandRouteImpl implements CommandRoute {

    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private final Set<String> namesAndAliasesLowerCase = new HashSet<>();

    private final List<CommandExecutor> executors = new ArrayList<>();
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
        return name;
    }

    @Override
    public List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }

    @Override
    public boolean isNameOrAlias(String name) {
        return namesAndAliasesLowerCase.contains(name.toLowerCase(Locale.ROOT));
    }

    @Override
    public List<CommandRoute> getChildren() {
        return Collections.unmodifiableList(childRoutes);
    }

    @Override
    public List<CommandExecutor> getExecutors() {
        return Collections.unmodifiableList(executors);
    }

    @Override
    public void appendChildren(CommandRoute children) {
        this.childRoutes.add(children);
    }

    @Override
    public void appendExecutor(CommandExecutor executor) {
        this.executors.add(executor);
    }

}
