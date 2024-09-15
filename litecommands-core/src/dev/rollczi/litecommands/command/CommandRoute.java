package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.priority.PrioritizedList;
import dev.rollczi.litecommands.scope.Scopeable;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * CommandRoute is a node of the command tree.
 * Represents a command or subcommand with its executors and next children (subcommands).
 *
 * @see CommandExecutor
 */
public interface CommandRoute<SENDER> extends Scopeable, CommandNode<SENDER> {

    String getName();

    UUID getUniqueId();

    @Unmodifiable
    List<String> getAliases();

    @Unmodifiable
    List<String> names();

    @Contract("null -> false; !null -> _")
    boolean isNameOrAlias(String name);

    CommandRoute<SENDER> getParent();

    default boolean isRoot() {
        return false;
    }

    default boolean isReference() {
        return false;
    }

    void appendChildren(CommandRoute<SENDER> children);

    @Unmodifiable
    List<CommandRoute<SENDER>> getChildren();

    Optional<CommandRoute<SENDER>> getChild(String name);

    void appendExecutor(CommandExecutor<SENDER> executor);

    @Unmodifiable
    PrioritizedList<CommandExecutor<SENDER>> getExecutors();

    Meta meta();

    static <SENDER> CommandRoute<SENDER> create(CommandRoute<SENDER> parent, String name, List<String> aliases) {
        return new CommandRouteImpl<>(name, aliases, parent);
    }

    static <SENDER> CommandRoute<SENDER> createRoot() {
        return new CommandRootRouteImpl<>();
    }

    @ApiStatus.Experimental
    CommandRoute<SENDER> merge(CommandRoute<SENDER> toMerge);

}
