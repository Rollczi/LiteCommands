package dev.rollczi.litecommands.annotations.command.executor;

import dev.rollczi.litecommands.command.requirement.Requirement;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodDefinition<SENDER> {

    private final Map<Integer, Requirement<SENDER, ?>> requirements = new HashMap<>();


    public void putRequirement(int parameterIndex, Requirement<SENDER, ?> requirement) {
        requirements.put(parameterIndex, requirement);
    }

    public Requirement<SENDER, ?> getRequirement(int parameterIndex) {
        return requirements.get(parameterIndex);
    }

    public Collection<Requirement<SENDER, ?>> getRequirements() {
        return requirements.values();
    }
}
