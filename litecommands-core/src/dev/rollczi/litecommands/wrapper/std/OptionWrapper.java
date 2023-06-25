package dev.rollczi.litecommands.wrapper.std;

import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import panda.std.Option;

import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public class OptionWrapper extends AbstractWrapper<Option> implements Wrapper {

    public OptionWrapper() {
        super(Option.class);
    }

    @Override
    protected <EXPECTED> Supplier<Option> wrapValue(EXPECTED valueToWrap, WrapFormat<EXPECTED, ?> info) {
        return () -> Option.of(valueToWrap);
    }

    @Override
    protected <EXPECTED> Supplier<Option> emptyValue(WrapFormat<EXPECTED, ?> info) {
        return Option::none;
    }

    @Override
    public boolean canCreateEmpty() {
        return true;
    }

}
