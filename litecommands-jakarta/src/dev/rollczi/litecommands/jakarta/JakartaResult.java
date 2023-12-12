package dev.rollczi.litecommands.jakarta;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class JakartaResult {

    private final Set<ConstraintViolation<Object>> violations;

    public JakartaResult(Set<ConstraintViolation<Object>> violations) {
        this.violations = violations;
    }

    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }

}
