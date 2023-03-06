package dev.rollczi.litecommands.modern.validator;

import dev.rollczi.litecommands.modern.util.Preconditions;
import org.jetbrains.annotations.Nullable;

public class CommandValidatorResult {

    private final boolean valid;
    private final boolean canBeIgnored;
    private final @Nullable Object invalidResult;

    private CommandValidatorResult(boolean valid, boolean canBeIgnored, @Nullable Object invalidResult) {
        this.valid = valid;
        this.canBeIgnored = canBeIgnored;
        this.invalidResult = invalidResult;
    }

    public boolean isInvalid() {
        return !valid;
    }

    public boolean canBeIgnored() {
        return canBeIgnored;
    }

    public boolean hasInvalidResult() {
        return invalidResult != null;
    }

    public Object getInvalidResult() {
        Preconditions.checkState(!valid, "Command is valid");
        Preconditions.notNull(invalidResult, "invalid result");

        return invalidResult;
    }

    public static CommandValidatorResult valid() {
        return new CommandValidatorResult(true, false, null);
    }

    public static CommandValidatorResult invalid(boolean canBeIgnored) {
        return new CommandValidatorResult(false, canBeIgnored, null);
    }

    public static CommandValidatorResult invalid(Object invalidResult, boolean canBeIgnored) {
        return new CommandValidatorResult(false, canBeIgnored, invalidResult);
    }

}
