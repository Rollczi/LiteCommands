package dev.rollczi.litecommands.wrapper;

import dev.rollczi.litecommands.wrapper.implementations.ValueWrappedExpectedFactory;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class WrappedExpectedService {

    private final Map<Class<?>, WrappedExpectedFactory> factories = new HashMap<>();
    private final WrappedExpectedFactory defaultFactory = new ValueWrappedExpectedFactory();

    public void registerFactory(WrappedExpectedFactory factory) {
        this.factories.put(factory.getWrapperType(), factory);
    }

    public <EXPECTED> WrappedExpected<EXPECTED> wrap(ValueToWrap<EXPECTED> result, WrapperFormat<EXPECTED, ?> wrapperFormat) {
        WrappedExpectedFactory factory = this.getWrappedExpectedFactory(wrapperFormat);

        return factory.create(result, wrapperFormat);
    }

    public <EXPECTED> Option<WrappedExpected<EXPECTED>> empty(WrapperFormat<EXPECTED, ?> wrapperFormat) {
        WrappedExpectedFactory factory = this.getWrappedExpectedFactory(wrapperFormat);

        if (factory.canCreateEmpty()) {
            return Option.of(factory.createEmpty(wrapperFormat));
        }

        return Option.none();
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return this.factories.containsKey(expectedType);
    }

    public WrappedExpectedFactory getWrappedExpectedFactory(WrapperFormat<?, ?> wrapperFormat) {
        if (!wrapperFormat.hasWrapper()) {
            return this.defaultFactory;
        }

        WrappedExpectedFactory factory = this.factories.get(wrapperFormat.getWrapperType());

        if (factory == null) {
            factory = this.defaultFactory;
        }

        return factory;
    }

}
