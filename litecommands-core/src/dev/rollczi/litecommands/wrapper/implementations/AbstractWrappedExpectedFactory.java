package dev.rollczi.litecommands.wrapper.implementations;

import dev.rollczi.litecommands.wrapper.WrapperFormat;
import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.WrappedExpected;
import dev.rollczi.litecommands.wrapper.WrappedExpectedFactory;

import java.util.Optional;
import java.util.function.Supplier;

abstract class AbstractWrappedExpectedFactory<WRAPPER> implements WrappedExpectedFactory {

    private final Class<WRAPPER> wrapperType;

    protected AbstractWrappedExpectedFactory(Class<WRAPPER> wrapperType) {
        this.wrapperType = wrapperType;
    }

    @Override
    public Class<WRAPPER> getWrapperType() {
        return this.wrapperType;
    }

    @Override
    public <EXPECTED> WrappedExpected<EXPECTED> create(ValueToWrap<EXPECTED> valueToWrap, WrapperFormat<EXPECTED, ?> info) {
        this.check(info);

        return new TypeSafeWrappedExpected<>(info.getType(), this.wrapValue(valueToWrap, info));
    }

    protected abstract <EXPECTED> Supplier<WRAPPER> wrapValue(ValueToWrap<EXPECTED> valueToWrap, WrapperFormat<EXPECTED, ?> info);

    @Override
    public <EXPECTED> WrappedExpected<EXPECTED> createEmpty(WrapperFormat<EXPECTED, ?> info) {
        this.check(info);

        return new TypeSafeWrappedExpected<>(info.getType(), this.emptyValue(info));
    }

    protected abstract <EXPECTED> Supplier<WRAPPER> emptyValue(WrapperFormat<EXPECTED, ?> info);

    @Override
    public boolean canCreateEmpty() {
        return this.canCreateEmptyValue();
    }

    protected abstract boolean canCreateEmptyValue();

    private void check(WrapperFormat<?, ?> info) {
        if (!info.hasWrapper()) {
            throw new IllegalArgumentException("Cannot wrap value without wrapper");
        }

        if (!info.getWrapperType().equals(Optional.class)) {
            throw new IllegalArgumentException("Wrapper type mismatch");
        }
    }

    private class TypeSafeWrappedExpected<EXPECTED> implements WrappedExpected<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<WRAPPER> wrapperSupplier;

        TypeSafeWrappedExpected(Class<EXPECTED> expectedType, Supplier<WRAPPER> wrapperSupplier) {
            this.expectedType = expectedType;
            this.wrapperSupplier = wrapperSupplier;
        }

        @Override
        public WRAPPER unwrap() {
            return wrapperSupplier.get();
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }

    }

}
