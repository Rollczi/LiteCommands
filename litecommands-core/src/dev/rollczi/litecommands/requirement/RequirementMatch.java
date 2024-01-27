package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.wrapper.Wrap;

public class RequirementMatch {

    private final Requirement<?> requirement;
    private final Wrap<?> result;

    public RequirementMatch(Requirement<?> requirement, Wrap<?> result) {
        this.requirement = requirement;
        this.result = result;
    }

    public Requirement<?> getRequirement() {
        return requirement;
    }

    public Wrap<?> getResult() {
        return result;
    }

}
