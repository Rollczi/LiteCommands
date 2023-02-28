package dev.rollczi.litecommands.modern.wrapper.implementations;

import dev.rollczi.litecommands.modern.wrapper.ValueToWrap;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpected;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;
import panda.std.Option;

import java.util.function.Supplier;

public class ValueWrappedExpectedFactory implements WrappedExpectedFactory {

    @Override
    public <EXPECTED> WrappedExpected<EXPECTED> wrap(ValueToWrap<EXPECTED> valueToWrap, WrapperFormat<EXPECTED> info) {
        Class<EXPECTED> expectedType = info.getType();

        return new Expected<>(expectedType, valueToWrap);
    }

    @Override
    public <EXPECTED> Option<WrappedExpected<EXPECTED>> empty(WrapperFormat<EXPECTED> info) {
        return Option.none();
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class Expected<EXPECTED> implements WrappedExpected<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public Expected(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
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
