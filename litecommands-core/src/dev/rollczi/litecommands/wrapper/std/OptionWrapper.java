package dev.rollczi.litecommands.wrapper.std;

import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import panda.std.Option;

import java.util.function.Supplier;

public class OptionWrapper implements Wrapper {

    @Override
    public <EXPECTED> Wrap<EXPECTED> create(ValueToWrap<EXPECTED> valueToWrap, WrapFormat<EXPECTED, ?> info) {
        Class<EXPECTED> expectedType = info.getParsedType();

        return new OptionWrap<>(expectedType, valueToWrap);
    }

    @Override
    public <EXPECTED> Wrap<EXPECTED> createEmpty(WrapFormat<EXPECTED, ?> info) {
        return new OptionWrap<>(info.getParsedType(), () -> null);
    }

    @Override
    public boolean canCreateEmpty() {
        return true;
    }

    @Override
    public Class<?> getWrapperType() {
        return Option.class;
    }

    private static class OptionWrap<EXPECTED> implements Wrap<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public OptionWrap(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
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
