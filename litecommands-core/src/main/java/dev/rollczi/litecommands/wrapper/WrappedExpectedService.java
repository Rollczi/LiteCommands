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

    public <EXPECTED> WrappedExpected<EXPECTED> wrap(ValueToWrap<EXPECTED> result, WrapperFormat<EXPECTED> info) {
        WrappedExpectedFactory factory = this.factories.get(info.getWrapperType());

        if (factory == null) {
            factory = this.defaultFactory;
        }


        return factory.wrap(result, info);
    }

    public <EXPECTED> Option<WrappedExpected<EXPECTED>> empty(WrapperFormat<EXPECTED> context) {
        WrappedExpectedFactory factory = this.factories.get(context.getWrapperType());

        if (factory == null) {
            factory = this.defaultFactory;
        }

        return factory.empty(context);
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return this.factories.containsKey(expectedType);
    }

}
