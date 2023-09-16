package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MethodDefinition<SENDER> {

    private final Map<Integer, Requirement<SENDER, ?>> requirements = new HashMap<>();

    void putRequirement(int parameterIndex, Requirement<SENDER, ?> requirement) {
        requirements.put(parameterIndex, requirement);
    }

    Requirement<SENDER, ?> getRequirement(int parameterIndex) {
        return requirements.get(parameterIndex);
    }

    Collection<Requirement<SENDER, ?>> getRequirements() {
        return requirements.values();
    }

    static <SENDER> MethodDefinition<SENDER> createDefinition(Method method, List<Requirement<SENDER, ?>> requirements) {
        MethodDefinition<SENDER> definition = new MethodDefinition<>();

        int index = 0;
        for (Parameter parameter : method.getParameters()) {
            Requirement<SENDER, ?> requirement = requirements.get(index);

            WrapFormat<?, ?> format = requirement.getAnnotationHolder().getFormat();
            Class<?> typeToCheck = format.getOutTypeOrParsed();
            Class<?> currentType = parameter.getType();

            if (!typeToCheck.equals(currentType)) {
                throw new IllegalStateException("Parameter type mismatch for " + parameter.getName() + " in " + method.getName() + " method (expected " + currentType.getSimpleName() + " but got " + typeToCheck.getSimpleName());
            }

            definition.putRequirement(index, requirement);
            index++;
        }

        return definition;
    }

}
