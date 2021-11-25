package dev.rollczi.litecommands.valid;

import dev.rollczi.litecommands.LiteInvocation;
import panda.utilities.StringUtils;

import java.util.function.Function;

public enum ValidationInfo {

    COMMAND_NO_FOUND(invocation -> "&cCommand or subcommand not found"),
    NO_PERMISSION(invocation -> "&cYou don't have permission to this command"),
    INVALID_USE(invocation -> "&cInvalid use of the command"),
    CUSTOM(invocation -> StringUtils.EMPTY);

    private final ContextMessage defaultMessage;

    ValidationInfo(ContextMessage defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public ContextMessage getDefaultMessage() {
        return defaultMessage;
    }

    @FunctionalInterface
    public interface ContextMessage extends Function<LiteInvocation, String> {

        @Override
        String apply(LiteInvocation invocation);

    }

}
