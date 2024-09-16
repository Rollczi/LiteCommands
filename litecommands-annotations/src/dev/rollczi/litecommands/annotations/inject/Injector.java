package dev.rollczi.litecommands.annotations.inject;

import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.bind.BindResult;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Injector {

    private final BindRegistry registry;

    public Injector(BindRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public <T> T createInstance(Class<T> type) {
        List<Constructor<?>> constructors = Arrays.stream(type.getDeclaredConstructors())
            .filter(constructor -> constructor.isAnnotationPresent(Inject.class) || constructor.getParameterCount() == 0)
            .sorted((o1, o2) -> Integer.compare(o2.getParameterCount(), o1.getParameterCount()))
            .collect(Collectors.toList());

        if (constructors.isEmpty()) {
            throw new LiteCommandsReflectInvocationException(type, "Missing constructor with @Inject annotation or without parameters");
        }

        List<LiteCommandsReflectInvocationException> exceptions = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            Object[] parameters = new Object[constructor.getParameterCount()];

            for (int i = 0; i < constructor.getParameterCount(); i++) {
                Parameter parameter = constructor.getParameters()[i];
                Class<?> parameterType = parameter.getType();
                BindResult<?> result = this.registry.getInstance(parameterType);

                if (result.isError()) {
                    exceptions.add(new LiteCommandsReflectInvocationException(constructor, parameter, "Cannot inject parameter " + parameterType.getName() + " error: " + result.getError()));
                    continue;
                }

                parameters[i] = result.getSuccess();
            }

            try {
                constructor.setAccessible(true);

                return (T) constructor.newInstance(parameters);
            }
            catch (Exception exception) {
                exceptions.add(new LiteCommandsReflectInvocationException(constructor, "Cannot create invoke construcotr", exception));
            }
        }

        throw new LiteCommandsException("Cannot create instance of " + type.getName(), exceptions);
    }

}
