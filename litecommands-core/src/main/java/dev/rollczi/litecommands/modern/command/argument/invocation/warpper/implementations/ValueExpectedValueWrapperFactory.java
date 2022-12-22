package dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations;

import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapperFactory;

import java.util.function.Supplier;

public class ValueExpectedValueWrapperFactory implements ExpectedValueWrapperFactory {

    @Override
    public <EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(Class<EXPECTED> type, Supplier<EXPECTED> value) {
        return new ValueWrapper<>(type, value.get());
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class ValueWrapper<EXPECTED> implements ExpectedValueWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final EXPECTED value;

        public ValueWrapper(Class<EXPECTED> expectedType, EXPECTED value) {
            this.expectedType = expectedType;
            this.value = value;
        }

        @Override
        public Object getWrappedValue() {
            return value;
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }
    }

}
