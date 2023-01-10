package dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.FailedReason;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapperFactory;
import panda.std.Result;

import java.util.function.Supplier;

public class ValueExpectedValueWrapperFactory implements ExpectedValueWrapperFactory {

    @Override
    public <DETERMINANT, EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(ArgumentResult<EXPECTED> result, ArgumentContext<DETERMINANT, EXPECTED> context) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        if (result.isSuccessful()) {
            SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

            return new ValueWrapper<>(expectedType, successfulResult.getParsedArgument());
        }

        throw new IllegalArgumentException("Result can not be failed");
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class ValueWrapper<EXPECTED> implements ExpectedValueWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public ValueWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
            this.expectedType = expectedType;
            this.expectedSupplier = expectedSupplier;
        }

        @Override
        public EXPECTED getWrappedValue() {
            return expectedSupplier.get();
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }
    }

}
