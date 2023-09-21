package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.wrapper.Wrap;

public class RequirementMatch<REQUIREMENT extends Requirement<? extends T>, T> {

    private final REQUIREMENT requirement;
    private final Wrap<T> result;

    public RequirementMatch(REQUIREMENT requirement, Wrap<T> result) {
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
