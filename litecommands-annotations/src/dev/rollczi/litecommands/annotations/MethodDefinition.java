package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodDefinition {

    private final Method method;
    private final Map<Integer, Argument<?>> arguments = new HashMap<>();
    private final Map<Integer, ContextRequirement<?>> contextRequirements = new HashMap<>();
    private final Map<Integer, BindRequirement<?>> bindRequirements = new HashMap<>();

    private List<Requirement<?>> indexedRequirements;

    MethodDefinition(Method method) {
        this.method = method;
    }

    public Requirement<?> getRequirement(int parameterIndex) {
        return getRequirements().get(parameterIndex);
    }

    public List<Requirement<?>> getRequirements() {
        if (indexedRequirements == null) {
            List<Requirement<?>> indexedRequirements = new ArrayList<>();

            for (int i = 0; i < method.getParameterCount(); i++) {
                indexedRequirements.add(getRequirement0(i));
            }

            this.indexedRequirements = indexedRequirements;
            return indexedRequirements;
        }

        return indexedRequirements;
    }

    private Requirement<?> getRequirement0(int parameterIndex) {
        Argument<?> argument = arguments.get(parameterIndex);

        if (argument != null) {
            return argument;
        }

        ContextRequirement<?> contextRequirement = contextRequirements.get(parameterIndex);

        if (contextRequirement != null) {
            return contextRequirement;
        }

        BindRequirement<?> bindRequirement = bindRequirements.get(parameterIndex);

        if (bindRequirement != null) {
            return bindRequirement;
        }

        throw new IllegalArgumentException("Cannot find requirement for parameter index " + parameterIndex);
    }

    boolean hasRequirement(int parameterIndex) {
        return arguments.containsKey(parameterIndex) || contextRequirements.containsKey(parameterIndex) || bindRequirements.containsKey(parameterIndex);
    }

    public Collection<Argument<?>> getArguments() {
        return arguments.values();
    }

    public Collection<ContextRequirement<?>> getContextRequirements() {
        return contextRequirements.values();
    }

    public Collection<BindRequirement<?>> getBindRequirements() {
        return bindRequirements.values();
    }

    void putRequirement(int parameterIndex, Requirement<?> requirement) {
        WrapFormat<?, ?> wrapperFormat = requirement.getWrapperFormat();
        Class<?> typeOrParsed = wrapperFormat.getOutTypeOrParsed();
        Parameter parameter = method.getParameters()[parameterIndex];

        if (!typeOrParsed.isAssignableFrom(parameter.getType())) {
            throw new LiteCommandsReflectInvocationException(method, parameter, "Parameter type is not assignable from " + typeOrParsed.getSimpleName());
        }

        if (requirement instanceof Argument) {
            if (arguments.containsKey(parameterIndex)) {
                throw new LiteCommandsReflectInvocationException(method, parameter, "Cannot put argument on index " + parameterIndex + " because it is already occupied!");
            }

            arguments.put(parameterIndex, (Argument<?>) requirement);
            return;
        }

        if (requirement instanceof ContextRequirement) {
            if (contextRequirements.containsKey(parameterIndex)) {
                throw new LiteCommandsReflectInvocationException(method, parameter, "Cannot put context requirement on index " + parameterIndex + " because it is already occupied!");
            }

            contextRequirements.put(parameterIndex, (ContextRequirement<?>) requirement);
            return;
        }

        if (requirement instanceof BindRequirement) {
            if (bindRequirements.containsKey(parameterIndex)) {
                throw new LiteCommandsReflectInvocationException(method, parameter, "Cannot put bind requirement on index " + parameterIndex + " because it is already occupied!");
            }

            bindRequirements.put(parameterIndex, (BindRequirement<?>) requirement);
            return;
        }

        throw new LiteCommandsReflectInvocationException(method, parameter, "Cannot put requirement on index " + parameterIndex + " because it is not supported!");
    }

}
