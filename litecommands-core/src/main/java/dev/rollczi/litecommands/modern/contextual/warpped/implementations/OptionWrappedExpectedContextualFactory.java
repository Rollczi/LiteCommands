package dev.rollczi.litecommands.modern.contextual.warpped.implementations;

import dev.rollczi.litecommands.modern.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualFactory;
import panda.std.Option;

import java.util.function.Supplier;

public class OptionWrappedExpectedContextualFactory implements WrappedExpectedContextualFactory {

    @Override
    public <EXPECTED> WrappedExpectedContextual<EXPECTED> wrap(ExpectedContextualProvider<EXPECTED> expectedContextualProvider, ExpectedContextual<EXPECTED> context) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        return new OptionWrapper<>(expectedType, expectedContextualProvider);
    }

    @Override
    public <EXPECTED> Option<WrappedExpectedContextual<EXPECTED>> empty(ExpectedContextual<EXPECTED> context) {
        return Option.of(new OptionWrapper<>(context.getExpectedType(), () -> null));
    }

    @Override
    public Class<?> getWrapperType() {
        return Option.class;
    }

    private static class OptionWrapper<EXPECTED> implements WrappedExpectedContextual<EXPECTED> {

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
