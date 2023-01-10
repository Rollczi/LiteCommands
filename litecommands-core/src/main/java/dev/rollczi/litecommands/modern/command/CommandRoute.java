package dev.rollczi.litecommands.modern.command;

import java.util.List;

public interface CommandRoute {

    String getName();

    List<String> getAliases();

    boolean isNameOrAlias(String name);

    List<CommandRoute> getChildren();

    List<CommandExecutor> getExecutors();

    void appendChildren(CommandRoute children);

    void appendExecutor(CommandExecutor executor);

    default boolean isRoot() {
        return false;
    }

}
