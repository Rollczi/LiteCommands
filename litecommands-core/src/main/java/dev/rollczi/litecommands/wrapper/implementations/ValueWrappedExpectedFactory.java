package dev.rollczi.litecommands.wrapper.implementations;

import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import dev.rollczi.litecommands.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.wrapper.WrapperFormat;
import panda.std.Option;

import java.util.function.Supplier;

public class ValueWrappedExpectedFactory implements WrappedExpectedFactory {

    @Override
    public <EXPECTED> WrappedExpected<EXPECTED> create(ValueToWrap<EXPECTED> valueToWrap, WrapperFormat<EXPECTED, ?> info) {
        Class<EXPECTED> expectedType = info.getType();

        return new ValueWrappedExpected<>(expectedType, valueToWrap);
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class ValueWrappedExpected<EXPECTED> implements WrappedExpected<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public ValueWrappedExpected(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
            this.expectedType = expectedType;
            this.expectedSupplier = expectedSupplier;
        }

        @Override
        public EXPECTED unwrap() {
            return this.expectedSupplier.get();
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return this.expectedType;
        }

    }

}
