package dev.rollczi.litecommands.command.executor.flow;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the flow of the command execution.
 */
@ApiStatus.Experimental
public enum ExecuteFlow {

    /**
     * Continue the flow.
     */
    CONTINUE,

    /**
     * Skip the flow and continue to the next one.
     */
    SKIP,

    /**
     * Terminate the flow. (Stop the execution)
     */
    STOP,

}
