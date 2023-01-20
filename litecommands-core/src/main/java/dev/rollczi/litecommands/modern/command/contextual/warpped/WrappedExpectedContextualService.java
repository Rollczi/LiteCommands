package dev.rollczi.litecommands.modern.command.contextual.warpped;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.implementations.ValueWrappedExpectedContextualFactory;
import panda.std.Option;

import java.util.HashMap;
import java.util.Map;

public class WrappedExpectedContextualService {

    private final Map<Class<?>, WrappedExpectedContextualFactory> factories = new HashMap<>();
    private final WrappedExpectedContextualFactory defaultFactory = new ValueWrappedExpectedContextualFactory();

    public void registerFactory(WrappedExpectedContextualFactory factory) {
        this.factories.put(factory.getWrapperType(), factory);
    }

    public <EXPECTED> WrappedExpectedContextual<EXPECTED> wrap(ExpectedContextualProvider<EXPECTED> result, ExpectedContextual<EXPECTED> context) {
        WrappedExpectedContextualFactory factory = this.factories.get(context.getExpectedWrapperType());

        if (factory == null) {
            factory = this.defaultFactory;
        }


        return factory.wrap(result, context);
    }

    public <EXPECTED> Option<WrappedExpectedContextual<EXPECTED>> empty(ExpectedContextual<EXPECTED> context) {
        WrappedExpectedContextualFactory factory = this.factories.get(context.getExpectedWrapperType());

        if (factory == null) {
            factory = this.defaultFactory;
        }

        return factory.empty(context);
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return this.factories.containsKey(expectedType);
    }

}
