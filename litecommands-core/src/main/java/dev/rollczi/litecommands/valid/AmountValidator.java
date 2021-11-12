package dev.rollczi.litecommands.valid;

@FunctionalInterface
public interface AmountValidator {

    boolean valid(int length);

    AmountValidator NONE = length -> true;

    default AmountValidator min(int min) {
        return this.and(length -> length >= min);
    }

    default AmountValidator max(int max) {
        return this.and(length -> length <= max);
    }

    default AmountValidator equals(int required) {
        return length -> length == required;
    }

    default AmountValidator and(AmountValidator validator) {
        if (this.equals(NONE)) {
            return validator;
        }

        return length -> this.valid(length) && validator.valid(length);
    }

    default AmountValidator or(AmountValidator validator) {
        if (this.equals(NONE)) {
            return validator;
        }

        return length -> this.valid(length) || validator.valid(length);
    }

}
