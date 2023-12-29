package dev.rollczi.litecommands.jakarta;

import java.util.List;

public class JakartaParsedResult {

    private final List<String> violations;

    public JakartaParsedResult(List<String> violations) {
        this.violations = violations;
    }

    public List<String> getViolations() {
        return violations;
    }

    public String asJoinedString() {
        return String.join("\n", violations);
    }

    public String asJoinedString(String delimiter) {
        return String.join(delimiter, violations);
    }

    @Override
    public String toString() {
        return "JakartaParsedResult{" +
            "violations=" + violations +
            '}';
    }

}
