package dev.rollczi.litecommands.modern.command.argument.invocation.warpped;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.implementations.ValueWrappedArgumentFactory;

import java.util.HashMap;
import java.util.Map;

public class WrappedArgumentService {

    private final Map<Class<?>, WrappedArgumentFactory> factories = new HashMap<>();
    private final WrappedArgumentFactory defaultFactory = new ValueWrappedArgumentFactory();

    public void registerFactory(WrappedArgumentFactory factory) {
        factories.put(factory.getWrapperType(), factory);
    }

    public <DETERMINANT, EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(ArgumentResult<EXPECTED> result, ArgumentContext<DETERMINANT, EXPECTED> context) {
        WrappedArgumentFactory factory = factories.get(context.getExpectedWrapperType());

        if (factory == null) {
            factory = defaultFactory;
        }


        return factory.wrap(result, context);
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return factories.containsKey(expectedType);
    }

}
