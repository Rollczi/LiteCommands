package dev.rollczi.litecommands.modern.command.contextual.warpped;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.implementations.ValueWrappedArgumentFactory;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class WrappedArgumentService {

    private final Map<Class<?>, WrappedArgumentFactory> factories = new HashMap<>();
    private final WrappedArgumentFactory defaultFactory = new ValueWrappedArgumentFactory();

    public void registerFactory(WrappedArgumentFactory factory) {
        factories.put(factory.getWrapperType(), factory);
    }

    public <EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(ExpectedContextualProvider<EXPECTED> result, ExpectedContextual<EXPECTED> context) {
        WrappedArgumentFactory factory = factories.get(context.getExpectedWrapperType());

        if (factory == null) {
            factory = defaultFactory;
        }


        return factory.wrap(result, context);
    }

    public <EXPECTED> Option<WrappedArgumentWrapper<EXPECTED>> empty(ExpectedContextual<EXPECTED> context) {
        WrappedArgumentFactory factory = factories.get(context.getExpectedWrapperType());

        if (factory == null) {
            factory = defaultFactory;
        }

        return factory.empty(context);
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return factories.containsKey(expectedType);
    }

}
