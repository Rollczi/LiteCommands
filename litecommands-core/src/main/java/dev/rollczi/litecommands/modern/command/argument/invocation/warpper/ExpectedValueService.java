package dev.rollczi.litecommands.modern.command.argument.invocation.warpper;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations.ValueExpectedValueWrapperFactory;
import panda.std.Result;

import java.util.HashMap;
import java.util.Map;

public class ExpectedValueService {

    private final Map<Class<?>, ExpectedValueWrapperFactory> factories = new HashMap<>();
    private final ExpectedValueWrapperFactory defaultFactory = new ValueExpectedValueWrapperFactory();

    public <T> void registerFactory(ExpectedValueWrapperFactory factory) {
        factories.put(factory.getWrapperType(), factory);
    }

    public <DETERMINANT, EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(ArgumentResult<EXPECTED> result, ArgumentContext<DETERMINANT, EXPECTED> context) {
        ExpectedValueWrapperFactory factory = factories.get(context.getExpectedWrapperType());

        if (factory == null) {
            factory = defaultFactory;
        }


        return factory.wrap(result, context);
    }

    public <EXPECTED> boolean isWrapper(Class<EXPECTED> expectedType) {
        return factories.containsKey(expectedType);
    }

}
