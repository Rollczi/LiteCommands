package dev.rollczi.litecommands.modern.contextual.warpped.implementations;

import dev.rollczi.litecommands.modern.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualFactory;
import panda.std.Option;

import java.util.function.Supplier;

public class ValueWrappedExpectedContextualFactory implements WrappedExpectedContextualFactory {

    @Override
    public <EXPECTED> WrappedExpectedContextual<EXPECTED> wrap(ExpectedContextualProvider<EXPECTED> expectedContextualProvider, dev.rollczi.litecommands.modern.contextual.ExpectedContextual<EXPECTED> context) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        return new ExpectedContextual<>(expectedType, expectedContextualProvider);
    }

    @Override
    public <EXPECTED> Option<WrappedExpectedContextual<EXPECTED>> empty(dev.rollczi.litecommands.modern.contextual.ExpectedContextual<EXPECTED> context) {
        return Option.none();
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class ExpectedContextual<EXPECTED> implements WrappedExpectedContextual<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public ExpectedContextual(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
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
