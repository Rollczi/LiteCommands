package dev.rollczi.litecommands.modern.extension.annotation.inject;

import dev.rollczi.litecommands.modern.command.Invocation;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Injector<SENDER> {

    private final InjectBindRegistry<SENDER> registry;

    public Injector(InjectBindRegistry<SENDER> registry) {
        this.registry = registry;
    }

    public <T> T createInstance(Class<T> type) {
        return createInstance(type, registry::getInstance);
    }

    public <T> T createInstance(Class<T> type, Invocation<SENDER> invocation) {
        return createInstance(type, parameterType -> registry.getInstance(parameterType, invocation));
    }

    @SuppressWarnings("unchecked")
    private  <T> T createInstance(Class<T> type, Function<Class<?>, Object> instanceProvider) {
        List<Constructor<?>> constructors = Arrays.stream(type.getConstructors())
            .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
            .sorted((o1, o2) -> Integer.compare(o2.getParameterCount(), o1.getParameterCount()))
            .collect(Collectors.toList());

        if (constructors.isEmpty()) {
            throw new IllegalStateException("No constructor with @Inject annotation for " + type.getName());
        }

        List<InjectorException> exceptions = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            Object[] parameters = new Object[constructor.getParameterCount()];

            for (int i = 0; i < constructor.getParameterCount(); i++) {
                Class<?> parameterType = constructor.getParameterTypes()[i];
                Object parameter = instanceProvider.apply(parameterType);

                if (parameter == null) {
                    exceptions.add(new InjectorException("Cannot inject parameter " + parameterType.getName() + " for " + type.getName()));
                    continue;
                }

                parameters[i] = parameter;
            }

            try {
                return (T) constructor.newInstance(parameters);
            } catch (Exception e) {
                exceptions.add(new InjectorException("Cannot create instance of " + type.getName(), e));
            }
        }

        throw new InjectorException("Constructor not found for " + type.getName(), exceptions);
    }

}
