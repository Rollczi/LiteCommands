package dev.rollczi.litecommands.wrapper.std;

import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public class OptionalWrapper extends AbstractWrapper<Optional> {

    public OptionalWrapper() {
        super(Optional.class);
    }

    @Override
    protected <EXPECTED> Supplier<Optional> wrapValue(EXPECTED valueToWrap, WrapFormat<EXPECTED, ?> info) {
        return () -> Optional.ofNullable(valueToWrap);
    }

    @Override
    protected <EXPECTED> Supplier<Optional> emptyValue(WrapFormat<EXPECTED, ?> info) {
        return Optional::empty;
    }

    @Override
    public boolean canCreateEmpty() {
        return true;
    }

}
