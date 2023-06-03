package dev.rollczi.litecommands.wrapper.implementations;

import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.function.Supplier;

public class ValueWrapper implements Wrapper {

    @Override
    public <EXPECTED> Wrap<EXPECTED> create(ValueToWrap<EXPECTED> valueToWrap, WrapFormat<EXPECTED, ?> info) {
        Class<EXPECTED> expectedType = info.getParsedType();

        return new ValueWrap<>(expectedType, valueToWrap);
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class ValueWrap<EXPECTED> implements Wrap<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public ValueWrap(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
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
