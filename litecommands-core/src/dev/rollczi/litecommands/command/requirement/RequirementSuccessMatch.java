package dev.rollczi.litecommands.command.requirement;

import dev.rollczi.litecommands.wrapper.Wrap;

public class RequirementSuccessMatch<SENDER, REQUIREMENT extends Requirement<SENDER, ? extends T>, T> {

    private final REQUIREMENT requirement;
    private final Wrap<T> result;

    public RequirementSuccessMatch(REQUIREMENT requirement, Wrap<T> result) {
        this.requirement = requirement;
        this.result = result;
    }

    public REQUIREMENT getRequirement() {
        return requirement;
    }

    public Wrap<T> getResult() {
        return result;
    }

}
