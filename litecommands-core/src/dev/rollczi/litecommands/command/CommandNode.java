package dev.rollczi.litecommands.command;

/**
 * CommandNode is an any node of the command tree.
 * @see CommandRoute
 * @see dev.rollczi.litecommands.command.executor.CommandExecutor
 */
public interface CommandNode<SENDER> {

    CommandRoute<SENDER> getParent();

}
