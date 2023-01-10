package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.shared.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CommandRootRouteImpl implements CommandRoute {

    private final List<CommandRoute> children = new ArrayList<>();

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
    public List<CommandRoute> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public List<CommandExecutor> getExecutors() {
        throw new UnsupportedOperationException("Can not get executors from the root route");
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
        this.children.add(children);
    }

}
