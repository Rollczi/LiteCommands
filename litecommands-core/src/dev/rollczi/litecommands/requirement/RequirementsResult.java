package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.invocation.Invocation;

import java.util.HashMap;
import java.util.Map;

public class RequirementsResult<SENDER> {

    private final Invocation<SENDER> invocation;
    private final Map<String, RequirementMatch<?, ?>> matches;

    private RequirementsResult(Invocation<SENDER> invocation, Map<String, RequirementMatch<?, ?>> matches) {
        this.invocation = invocation;
        this.matches = matches;
    }

    public boolean has(String name) {
        return matches.containsKey(name);
    }

    public RequirementMatch<?, ?> get(String name) {
        return matches.get(name);
    }

    public Invocation<SENDER> getInvocation() {
        return invocation;
    }

    public static <SENDER> Builder<SENDER> builder(Invocation<SENDER> invocation) {
        return new Builder<>(invocation);
    }

    public static class Builder<SENDER> {

        private final Invocation<SENDER> invocation;
        private final Map<String, RequirementMatch<?, ?>> matches = new HashMap<>();

        public Builder(Invocation<SENDER> invocation) {
            this.invocation = invocation;
        }

        public Builder<SENDER> add(String name, RequirementMatch<?, ?> match) {
            if (matches.containsKey(name)) {
                throw new IllegalArgumentException("Duplicate requirements name: " + name);
            }

            matches.put(name, match);
            return this;
        }

        public RequirementsResult<SENDER> build() {
            return new RequirementsResult<>(invocation, matches);
        }

    }

}
