package dev.rollczi.litecommands.wrapper.std;

import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.Optional;
import java.util.function.Supplier;

public class OptionalWrapper extends AbstractWrapper<Optional> {

    public OptionalWrapper() {
        super(Optional.class);
    }

    @Override
    protected <EXPECTED> Supplier<Optional> wrapValue(ValueToWrap<EXPECTED> valueToWrap, WrapFormat<EXPECTED, ?> info) {
        return () -> Optional.ofNullable(valueToWrap.get());
    }

    @Override
    protected <EXPECTED> Supplier<Optional> emptyValue(WrapFormat<EXPECTED, ?> info) {
        return Optional::empty;
    }

    @Override
    protected boolean canCreateEmptyValue() {
        return true;
    }

}
