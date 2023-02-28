package dev.rollczi.litecommands.modern.command;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommandRoute<SENDER> {

    String getName();

    List<String> getAliases();

    boolean isNameOrAlias(String name);

    Collection<CommandRoute<SENDER>> getChildren();

    Optional<CommandRoute<SENDER>> getChildren(String name);

    Collection<CommandExecutor<SENDER>> getExecutors();

    void appendChildren(CommandRoute<SENDER> children);

    void appendExecutor(CommandExecutor<SENDER> executor);

    default boolean isRoot() {
        return false;
    }

    static <SENDER> CommandRoute<SENDER> of(String name, List<String> aliases) {
        return new CommandRouteImpl<>(name, aliases);
    }

}
