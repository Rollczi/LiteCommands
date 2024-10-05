package dev.rollczi.litecommands.requirement;

public class RequirementMatch {

    private final Requirement<?> requirement;
    private final Object result;

    public RequirementMatch(Requirement<?> requirement, Object result) {
        this.requirement = requirement;
        this.result = result;
    }

    public Requirement<?> getRequirement() {
        return requirement;
    }

    public Object getResult() {
        return result;
    }

}
