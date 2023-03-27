package dev.rollczi.litecommands.wrapper.implementations;

import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.WrapperFormat;

import java.util.Optional;
import java.util.function.Supplier;

public class OptionalWrappedExpectedFactory extends AbstractWrappedExpectedFactory<Optional> {

    public OptionalWrappedExpectedFactory() {
        super(Optional.class);
    }

    @Override
    protected <EXPECTED> Supplier<Optional> wrapValue(ValueToWrap<EXPECTED> valueToWrap, WrapperFormat<EXPECTED, ?> info) {
        return () -> Optional.ofNullable(valueToWrap.get());
    }

    @Override
    protected <EXPECTED> Supplier<Optional> emptyValue(WrapperFormat<EXPECTED, ?> info) {
        return Optional::empty;
    }

    @Override
    protected boolean canCreateEmptyValue() {
        return true;
    }

}
