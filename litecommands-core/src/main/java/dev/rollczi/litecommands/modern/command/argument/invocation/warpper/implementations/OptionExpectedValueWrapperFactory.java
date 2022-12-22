package dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations;

import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapperFactory;
import panda.std.Option;

import java.util.function.Supplier;

public class OptionExpectedValueWrapperFactory implements ExpectedValueWrapperFactory {

    @Override
    public <EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(Class<EXPECTED> type, Supplier<EXPECTED> value) {
        return new OptionWrapper<>(type, value.get());
    }

    @Override
    public Class<?> getWrapperType() {
        return Option.class;
    }

    public static class OptionWrapper<EXPECTED> implements ExpectedValueWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final EXPECTED value;

        public OptionWrapper(Class<EXPECTED> expectedType, EXPECTED value) {
            this.expectedType = expectedType;
            this.value = value;
        }

        @Override
        public Object getWrappedValue() {
            return Option.of(value);
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }
    }

}
