package dev.rollczi.litecommands.valid;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.component.LiteComponent;
import panda.utilities.StringUtils;

public enum ValidationInfo {

    COMMAND_NO_EXIST((context, result) -> "&cCommand or subcommand not found"),
    NO_PERMISSION((context, result) -> "&cYou don't have permission to this command"),
    INVALID_USE((context, result) -> "&cInvalid use of the command"),
    CUSTOM,
    INTERNAL_ERROR,
    NONE;

    private final ContextMessage defaultMessage;

    ValidationInfo(ContextMessage defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    ValidationInfo() {
        this.defaultMessage = (context, result) -> StringUtils.EMPTY;
    }

    public ContextMessage getDefaultMessage() {
        return defaultMessage;
    }

    @FunctionalInterface
    public interface ContextMessage {

        String message(LiteComponent.ContextOfResolving context, ExecutionResult result);

    }

}
