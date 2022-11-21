package dev.rollczi.litecommands.command.count;

@FunctionalInterface
public interface CountValidator {

    boolean valid(int count);

    CountValidator NONE = count -> true;

    CountValidator POSITIVE = count -> count > 0;

    CountValidator POSITIVE_OR_ZERO = count -> count >= 0;

    CountValidator NEGATIVE = count -> count < 0;

    CountValidator NEGATIVE_OR_ZERO = count -> count <= 0;

    static CountValidator none() {
        return NONE;
    }

    static CountValidator positive() {
        return POSITIVE;
    }

    static CountValidator positiveOrZero() {
        return POSITIVE_OR_ZERO;
    }

    static CountValidator negative() {
        return NEGATIVE;
    }

    static CountValidator negativeOrZero() {
        return NEGATIVE_OR_ZERO;
    }

    default CountValidator min(int min) {
        return this.and(count -> count >= min);
    }

    default CountValidator max(int max) {
        return this.and(count -> count <= max);
    }

    default CountValidator required(int required) {
        return count -> count == required;
    }

    default CountValidator and(CountValidator validator) {
        if (this.equals(NONE)) {
            return validator;
        }

        return count -> this.valid(count) && validator.valid(count);
    }

    default CountValidator and(int value) {
        return this.and(this.required(value));
    }

    default CountValidator or(CountValidator validator) {
        if (this.equals(NONE)) {
            return validator;
        }

        return count -> this.valid(count) || validator.valid(count);
    }

    default CountValidator or(int value) {
        return this.or(this.required(value));
    }

}
