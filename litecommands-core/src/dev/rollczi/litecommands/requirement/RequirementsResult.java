package dev.rollczi.litecommands.requirement;

import java.util.HashMap;
import java.util.Map;

public class RequirementsResult<SENDER> {

    private final Map<String, RequirementMatch<SENDER, ?, ?>> matches;

    private RequirementsResult(Map<String, RequirementMatch<SENDER, ?, ?>> matches) {
        this.matches = matches;
    }

    public boolean has(String name) {
        return matches.containsKey(name);
    }

    public RequirementMatch<SENDER, ?, ?> get(String name) {
        return matches.get(name);
    }

    public static <SENDER> Builder<SENDER> builder() {
        return new Builder<>();
    }

    public static class Builder<SENDER> {

        private final Map<String, RequirementMatch<SENDER, ?, ?>> matches = new HashMap<>();

        public Builder<SENDER> add(String name, RequirementMatch<SENDER, ?, ?> match) {
            if (matches.containsKey(name)) {
                throw new IllegalArgumentException("Requirement match with name '" + name + "' already exists");
            }

            matches.put(name, match);
            return this;
        }

        public RequirementsResult<SENDER> build() {
            return new RequirementsResult<>(matches);
        }

    }

}
