package dev.rollczi.litecommands.modern.command;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommandRoute {

    String getName();

    List<String> getAliases();

    boolean isNameOrAlias(String name);

    Collection<CommandRoute> getChildren();

    Optional<CommandRoute> getChildren(String name);

    Collection<CommandExecutor> getExecutors();

    Optional<CommandExecutor> getExecutor(CommandExecutorKey key);

    void appendChildren(CommandRoute children);

    void appendExecutor(CommandExecutor executor);

    default boolean isRoot() {
        return false;
    }

}
