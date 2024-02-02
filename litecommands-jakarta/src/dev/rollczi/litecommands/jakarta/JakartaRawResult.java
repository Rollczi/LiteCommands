package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.requirement.Requirement;
import jakarta.validation.ConstraintViolation;

import java.util.List;

public class JakartaRawResult {

    private final List<Entry> entries;

    public JakartaRawResult(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getViolations() {
        return entries;
    }

    public static class Entry {

        private final ConstraintViolation<Object> violation;
        private final Requirement<?> requirement;

        public Entry(ConstraintViolation<Object> violation, Requirement<?> requirement) {
            this.violation = violation;
            this.requirement = requirement;
        }

        public ConstraintViolation<Object> getViolation() {
            return violation;
        }

        public Requirement<?> getRequirement() {
            return requirement;
        }

    }

}
