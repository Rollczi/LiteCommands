package dev.rollczi.litecommands.modern.command.argument.invocation.warpper;

import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations.ValueExpectedValueWrapperFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ExpectedValueService {

    private final Map<Class<?>, ExpectedValueWrapperFactory> factories = new HashMap<>();
    private final ExpectedValueWrapperFactory defaultFactory = new ValueExpectedValueWrapperFactory();

    public <T> void registerFactory(ExpectedValueWrapperFactory factory) {
        factories.put(factory.getWrapperType(), factory);
    }

    public <T> ExpectedValueWrapper<T> wrap(Class<?> wrapperType, Class<T> valueType, Supplier<T> supplier) {
        ExpectedValueWrapperFactory factory = factories.get(wrapperType);

        if (factory == null) {
            return defaultFactory.wrap(valueType, supplier);
        }

        return factory.wrap(valueType, supplier);
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return factories.containsKey(expectedType);
    }

}
