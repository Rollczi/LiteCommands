package dev.rollczi.litecommands.modern.argument.invocation.warpper;

import java.util.HashMap;
import java.util.Map;

public class ArgumentReturnWrapperService {

    private final Map<Class<?>, ArgumentReturnWrapperFactory<?>> factories = new HashMap<>();

    public <T> void registerFactory(Class<T> type, ArgumentReturnWrapperFactory<T> factory) {
        factories.put(type, factory);
    }

    public <T> ArgumentReturnWrapper<T> wrap(Class<?> type, T value) {
        ArgumentReturnWrapperFactory<T> factory = (ArgumentReturnWrapperFactory<T>) factories.get(type);

        if (factory == null) {
            throw new IllegalArgumentException("No factory for type " + type);
        }

        return factory.wrap(value);
    }

}
