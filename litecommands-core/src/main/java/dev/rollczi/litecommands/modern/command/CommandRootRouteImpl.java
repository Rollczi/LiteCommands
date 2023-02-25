package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.shared.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class CommandRootRouteImpl<SENDER> implements CommandRoute<SENDER> {

    private final Map<String, CommandRoute<SENDER>> children = new HashMap<>();

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
    public Collection<CommandRoute<SENDER>> getChildren() {
        return Collections.unmodifiableCollection(this.children.values());
    }

    @Override
    public Optional<CommandRoute<SENDER>> getChildren(String name) {
        CommandRoute<SENDER> route = this.children.get(name);

        if (route != null) {
            return Optional.of(route);
        }

        for (CommandRoute<SENDER> commandRoute : this.children.values()) {
            if (commandRoute.isNameOrAlias(name)) {
                return Optional.of(commandRoute);
            }
        }

        return Optional.empty();
    }

    @Override
    public Collection<CommandExecutor<SENDER>> getExecutors() {
        throw new UnsupportedOperationException("Can not get executors from the root route");
    }

    @Override
    public Optional<CommandExecutor<SENDER>> getExecutor(CommandExecutorKey key) {
        throw new UnsupportedOperationException("Can not get executor from the root route");
    }

    @Override
    public void appendChildren(CommandRoute<SENDER> children) {
        throw new UnsupportedOperationException("Can not append child route to the root route");
    }

    @Override
    public void appendExecutor(CommandExecutor<SENDER> executor) {
        throw new UnsupportedOperationException("Can not append command executor to the root route");
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @ApiStatus.Internal
    void appendToRoot(CommandRoute<SENDER> children) {
        this.children.put(children.getName(), children);
    }

}
