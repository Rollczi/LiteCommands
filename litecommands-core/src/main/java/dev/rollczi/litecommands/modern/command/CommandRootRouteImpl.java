package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.shared.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class CommandRootRouteImpl implements CommandRoute {

    private final Map<String, CommandRoute> children = new HashMap<>();

    @Override
    public String getName() {
        return StringUtils.EMPTY;
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean isNameOrAlias(String name) {
        return false;
    }

    @Override
    public Collection<CommandRoute> getChildren() {
        return Collections.unmodifiableCollection(this.children.values());
    }

    @Override
    public Optional<CommandRoute> getChildren(String name) {
        CommandRoute route = this.children.get(name);

        if (route != null) {
            return Optional.of(route);
        }

        for (CommandRoute commandRoute : this.children.values()) {
            if (commandRoute.isNameOrAlias(name)) {
                return Optional.of(commandRoute);
            }
        }

        return Optional.empty();
    }

    @Override
    public Collection<CommandExecutor> getExecutors() {
        throw new UnsupportedOperationException("Can not get executors from the root route");
    }

    @Override
    public Optional<CommandExecutor> getExecutor(CommandExecutorKey key) {
        throw new UnsupportedOperationException("Can not get executor from the root route");
    }

    @Override
    public void appendChildren(CommandRoute children) {
        throw new UnsupportedOperationException("Can not append child route to the root route");
    }

    @Override
    public void appendExecutor(CommandExecutor executor) {
        throw new UnsupportedOperationException("Can not append command executor to the root route");
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @ApiStatus.Internal
    void appendToRoot(CommandRoute children) {
        this.children.put(children.getName(), children);
    }

}
