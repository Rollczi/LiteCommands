package dev.rollczi.litecommands.jakarta;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.MessageInterpolator.Context;
import jakarta.validation.metadata.ConstraintDescriptor;

class JakartaConstraintViolationContext implements Context {

    private final ConstraintViolation<Object> violation;

    JakartaConstraintViolationContext(ConstraintViolation<Object> violation) {
        this.violation = violation;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return violation.getConstraintDescriptor();
    }

    @Override
    public Object getValidatedValue() {
        return violation.getInvalidValue();
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        return violation.unwrap(type);
    }

}
