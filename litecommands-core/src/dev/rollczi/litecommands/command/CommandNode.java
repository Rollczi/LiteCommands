package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.meta.MetaHolder;
import org.jetbrains.annotations.Nullable;

/**
 * CommandNode is an any node of the command tree.
 * @see CommandRoute
 * @see dev.rollczi.litecommands.command.executor.CommandExecutor
 */
public interface CommandNode<SENDER> extends MetaHolder {

    CommandRoute<SENDER> getParent();

    @Override
    @Nullable
    default MetaHolder parentMeta() {
        return getParent();
    }

}
