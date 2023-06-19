package dev.rollczi.litecommands.command.requirement;

public class RequirementMatch<REQUIREMENT> {

    private final REQUIREMENT requirement;
    private final CommandRequirementResult<?> result;

    public RequirementMatch(REQUIREMENT requirement, CommandRequirementResult<?> result) {
        this.requirement = requirement;
        this.result = result;
    }

    public REQUIREMENT getRequirement() {
        return requirement;
    }

    public CommandRequirementResult<?> getResult() {
        return result;
    }

}
