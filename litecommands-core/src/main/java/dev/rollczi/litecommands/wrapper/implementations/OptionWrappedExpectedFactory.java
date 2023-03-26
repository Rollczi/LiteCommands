package dev.rollczi.litecommands.wrapper.implementations;

import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import dev.rollczi.litecommands.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.wrapper.WrapperFormat;
import panda.std.Option;

import java.util.function.Supplier;

public class OptionWrappedExpectedFactory implements WrappedExpectedFactory {

    @Override
    public <EXPECTED> WrappedExpected<EXPECTED> wrap(ValueToWrap<EXPECTED> valueToWrap, WrapperFormat<EXPECTED> info) {
        Class<EXPECTED> expectedType = info.getType();

        return new OptionWrapper<>(expectedType, valueToWrap);
    }

    @Override
    public <EXPECTED> Option<WrappedExpected<EXPECTED>> empty(WrapperFormat<EXPECTED> info) {
        return Option.of(new OptionWrapper<>(info.getType(), () -> null));
    }

    @Override
    public Class<?> getWrapperType() {
        return Option.class;
    }

    private static class OptionWrapper<EXPECTED> implements WrappedExpected<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public OptionWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
            this.expectedType = expectedType;
            this.expectedSupplier = expectedSupplier;
        }

        @Override
        public Option<EXPECTED> unwrap() {
            return Option.of(this.expectedSupplier.get());
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return this.expectedType;
        }

    }

}
