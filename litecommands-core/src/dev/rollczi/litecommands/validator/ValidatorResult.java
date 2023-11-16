package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.shared.Preconditions;
import org.jetbrains.annotations.Nullable;

public class ValidatorResult {

    private final boolean valid;
    private final boolean canBeIgnored;
    private final @Nullable Object invalidResult;

    private ValidatorResult(boolean valid, boolean canBeIgnored, @Nullable Object invalidResult) {
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

    public static ValidatorResult valid() {
        return new ValidatorResult(true, false, null);
    }

    public static ValidatorResult invalid(Object invalidResult) {
        return new ValidatorResult(false, false, invalidResult);
    }

    public static ValidatorResult invalid(Object invalidResult, boolean canBeIgnored) {
        return new ValidatorResult(false, canBeIgnored, invalidResult);
    }

}
