package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.meta.CommandMeta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public interface CommandRoute<SENDER> {

    String getName();

    UUID getUniqueId();

    List<String> getAliases();

    List<String> getAllNames();

    boolean isNameOrAlias(String name);

    CommandRoute<SENDER> getParent();

    default boolean isRoot() {
        return false;
    }

    void appendChildren(CommandRoute<SENDER> children);

    List<CommandRoute<SENDER>> getChildren();

    Optional<CommandRoute<SENDER>> getChildren(String name);

    void appendExecutor(CommandExecutor<SENDER> executor);

    List<CommandExecutor<SENDER>> getExecutors();

    CommandMeta getMeta();

    static <SENDER> CommandRoute<SENDER> create(CommandRoute<SENDER> parent, String name, List<String> aliases) {
        return new CommandRouteImpl<>(name, aliases, parent);
    }

    static <SENDER> CommandRoute<SENDER> createRoot() {
        return new CommandRootRouteImpl<>();
    }

}
