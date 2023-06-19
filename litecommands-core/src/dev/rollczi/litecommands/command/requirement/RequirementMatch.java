package dev.rollczi.litecommands.command.requirement;

public class RequirementMatch<REQUIREMENT> {

    private final REQUIREMENT requirement;
    private final RequirementResult<?> result;

    public RequirementMatch(REQUIREMENT requirement, RequirementResult<?> result) {
        this.requirement = requirement;
        this.result = result;
    }

    public REQUIREMENT getRequirement() {
        return requirement;
    }

    public RequirementResult<?> getResult() {
        return result;
    }

}
