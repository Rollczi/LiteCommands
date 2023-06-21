package dev.rollczi.litecommands.wrapper;

import dev.rollczi.litecommands.wrapper.std.ValueWrapper;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class WrapperRegistry {

    private final Map<Class<?>, Wrapper> factories = new HashMap<>();
    private final Wrapper defaultFactory = new ValueWrapper();

    public void registerFactory(Wrapper factory) {
        this.factories.put(factory.getWrapperType(), factory);
    }

    public <EXPECTED> Wrap<EXPECTED> wrap(ValueToWrap<EXPECTED> result, WrapFormat<EXPECTED, ?> wrapFormat) {
        Wrapper factory = this.getWrappedExpectedFactory(wrapFormat);

        return factory.create(result.get(), wrapFormat);
    }

    public <EXPECTED> Option<Wrap<EXPECTED>> empty(WrapFormat<EXPECTED, ?> wrapFormat) {
        Wrapper factory = this.getWrappedExpectedFactory(wrapFormat);

        if (factory.canCreateEmpty()) {
            return Option.of(factory.createEmpty(wrapFormat));
        }

        return Option.none();
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return this.factories.containsKey(expectedType);
    }

    public Wrapper getWrappedExpectedFactory(WrapFormat<?, ?> wrapFormat) {
        if (!wrapFormat.hasOutType()) {
            return this.defaultFactory;
        }

        Wrapper factory = this.factories.get(wrapFormat.getOutType());

        if (factory == null) {
            factory = this.defaultFactory;
        }

        return factory;
    }

}
