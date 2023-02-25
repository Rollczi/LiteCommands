package dev.rollczi.litecommands.modern.command;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResolver;
import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;

public class ExecutableArgument<SENDER, DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> implements ExpectedContextual<EXPECTED> {

    private final ARGUMENT contextual;
    private final ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver;

    public ExecutableArgument(ARGUMENT contextual, ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> resolver) {
        this.contextual = contextual;
        this.resolver = resolver;
    }

    public ARGUMENT getContextual() {
        return contextual;
    }

    public ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT> getResolver() {
        return resolver;
    }

    @Override
    public Class<EXPECTED> getExpectedType() {
        return contextual.getExpectedType();
    }

    @Override
    public Class<?> getExpectedWrapperType() {
        return contextual.getExpectedWrapperType();
    }
}
