package dev.rollczi.litecommands.modern.command.argument.invocation.warpped.implementations;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentFactory;

import java.util.function.Supplier;

public class ValueWrappedArgumentFactory implements WrappedArgumentFactory {

    @Override
    public <DETERMINANT, EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(ArgumentResult<EXPECTED> result, ArgumentContext<DETERMINANT, EXPECTED> context) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        if (result.isSuccessful()) {
            SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

            return new ArgumentWrapper<>(expectedType, successfulResult.getParsedArgument());
        }

        throw new IllegalArgumentException("Result can not be failed");
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class ArgumentWrapper<EXPECTED> implements WrappedArgumentWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public ArgumentWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
            this.expectedType = expectedType;
            this.expectedSupplier = expectedSupplier;
        }

        @Override
        public EXPECTED unwrap() {
            return expectedSupplier.get();
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }
    }

}
