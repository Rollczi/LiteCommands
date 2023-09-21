package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class MethodDefinition {

    private final Method method;
    private final Map<Integer, Argument<?>> arguments = new HashMap<>();
    private final Map<Integer, ContextRequirement<?>> contextRequirements = new HashMap<>();

    MethodDefinition(Method method) {
        this.method = method;
    }

    Requirement<?> getRequirement(int parameterIndex) {
        Argument<?> argument = arguments.get(parameterIndex);

        if (argument != null) {
            return argument;
        }

        ContextRequirement<?> contextRequirement = contextRequirements.get(parameterIndex);

        if (contextRequirement != null) {
            return contextRequirement;
        }

        throw new IllegalArgumentException("Cannot find requirement for parameter index " + parameterIndex);
    }

    public Collection<Argument<?>> getArguments() {
        return arguments.values();
    }

    public Collection<ContextRequirement<?>> getContextRequirements() {
        return contextRequirements.values();
    }

    void putRequirement(int parameterIndex, Requirement<?> requirement) {
        WrapFormat<?, ?> wrapperFormat = requirement.getWrapperFormat();
        Class<?> typeOrParsed = wrapperFormat.getOutTypeOrParsed();
        Parameter parameter = method.getParameters()[parameterIndex];

        if (!typeOrParsed.isAssignableFrom(parameter.getType())) {
            throw new LiteCommandsReflectException(method, parameter, "Parameter type is not assignable from " + typeOrParsed.getSimpleName());
        }

        if (requirement instanceof Argument) {
            if (arguments.containsKey(parameterIndex)) {
                throw new IllegalArgumentException("Cannot put argument on index " + parameterIndex + " because it is already occupied!");
            }

            arguments.put(parameterIndex, (Argument<?>) requirement);
            return;
        }

        if (requirement instanceof ContextRequirement) {
            if (contextRequirements.containsKey(parameterIndex)) {
                throw new IllegalArgumentException("Cannot put context requirement on index " + parameterIndex + " because it is already occupied!");
            }

            contextRequirements.put(parameterIndex, (ContextRequirement<?>) requirement);
            return;
        }

        throw new IllegalArgumentException("Cannot put requirement on index " + parameterIndex + " because it is not supported!");
    }

}
