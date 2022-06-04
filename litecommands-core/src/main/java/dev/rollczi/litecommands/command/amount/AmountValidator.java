package dev.rollczi.litecommands.command.amount;

@FunctionalInterface
public interface AmountValidator {

    boolean valid(int length);

    AmountValidator NONE = length -> true;

    AmountValidator POSITIVE = length -> length > 0;

    AmountValidator POSITIVE_OR_ZERO = length -> length >= 0;

    AmountValidator NEGATIVE = length -> length < 0;

    AmountValidator NEGATIVE_OR_ZERO = length -> length <= 0;

    static AmountValidator none() {
        return NONE;
    }

    static AmountValidator positive() {
        return POSITIVE;
    }

    static AmountValidator positiveOrZero() {
        return POSITIVE_OR_ZERO;
    }

    static AmountValidator negative() {
        return NEGATIVE;
    }

    static AmountValidator negativeOrZero() {
        return NEGATIVE_OR_ZERO;
    }

    default AmountValidator min(int min) {
        return this.and(length -> length >= min);
    }

    default AmountValidator max(int max) {
        return this.and(length -> length <= max);
    }

    default AmountValidator required(int required) {
        return length -> length == required;
    }

    default AmountValidator and(AmountValidator validator) {
        if (this.equals(NONE)) {
            return validator;
        }

        return length -> this.valid(length) && validator.valid(length);
    }

    default AmountValidator and(int value) {
        return this.and(this.required(value));
    }

    default AmountValidator or(AmountValidator validator) {
        if (this.equals(NONE)) {
            return validator;
        }

        return length -> this.valid(length) || validator.valid(length);
    }

    default AmountValidator or(int value) {
        return this.or(this.required(value));
    }

}
