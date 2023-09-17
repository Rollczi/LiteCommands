package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.invocation.Invocation;

import java.util.HashMap;
import java.util.Map;

public class RequirementsResult<SENDER> {

    private final Invocation<SENDER> invocation;
    private final Map<String, RequirementMatch<SENDER, ?, ?>> matches;

    private RequirementsResult(Invocation<SENDER> invocation, Map<String, RequirementMatch<SENDER, ?, ?>> matches) {
        this.invocation = invocation;
        this.matches = matches;
    }

    public boolean has(String name) {
        return matches.containsKey(name);
    }

    public RequirementMatch<SENDER, ?, ?> get(String name) {
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
        private final Map<String, RequirementMatch<SENDER, ?, ?>> matches = new HashMap<>();

        public Builder(Invocation<SENDER> invocation) {
            this.invocation = invocation;
        }

        public Builder<SENDER> add(String name, RequirementMatch<SENDER, ?, ?> match) {
            if (matches.containsKey(name)) {
                throw new IllegalArgumentException("Requirement match with name '" + name + "' already exists");
            }

            matches.put(name, match);
            return this;
        }

        public RequirementsResult<SENDER> build() {
            return new RequirementsResult<>(invocation, matches);
        }

    }

}
