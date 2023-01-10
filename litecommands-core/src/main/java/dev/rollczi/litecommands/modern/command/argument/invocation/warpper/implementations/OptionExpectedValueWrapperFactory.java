package dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapperFactory;
import panda.std.Option;

import java.util.function.Supplier;

public class OptionExpectedValueWrapperFactory implements ExpectedValueWrapperFactory {

    @Override
    public <DETERMINANT, EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(ArgumentResult<EXPECTED> result, ArgumentContext<DETERMINANT, EXPECTED> context) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        if (result.isSuccessful()) {
            SuccessfulResult<EXPECTED> successfulResult = result.getSuccessfulResult();

            return new OptionWrapper<>(expectedType, successfulResult.getParsedArgument());
        }

        return new OptionWrapper<>(expectedType, null);
    }

    @Override
    public Class<?> getWrapperType() {
        return Option.class;
    }

    private static class OptionWrapper<EXPECTED> implements ExpectedValueWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public OptionWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
            this.expectedType = expectedType;
            this.expectedSupplier = expectedSupplier;
        }

        @Override
        public Option<EXPECTED> getWrappedValue() {
            return Option.of(expectedSupplier.get());
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }
    }

}
