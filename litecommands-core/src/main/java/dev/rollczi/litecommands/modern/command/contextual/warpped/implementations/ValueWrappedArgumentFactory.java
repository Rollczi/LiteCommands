package dev.rollczi.litecommands.modern.command.contextual.warpped.implementations;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentFactory;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import panda.std.Option;

import java.util.function.Supplier;

public class ValueWrappedArgumentFactory implements WrappedArgumentFactory {

    @Override
    public <EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(ExpectedContextualProvider<EXPECTED> expectedContextualProvider, ExpectedContextual<EXPECTED> context) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        return new ArgumentWrapper<>(expectedType, expectedContextualProvider);
    }

    @Override
    public <EXPECTED> Option<WrappedArgumentWrapper<EXPECTED>> empty(ExpectedContextual<EXPECTED> context) {
        return Option.none();
    }

    @Override
    public Class<?> getWrapperType() {
        return Void.class;
    }

    private static class ArgumentWrapper<EXPECTED> implements WrappedArgumentWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public ArgumentWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
            this.expectedType = expectedType;
            this.expectedSupplier = expectedSupplier;
        }

        @Override
        public EXPECTED unwrap() {
            return expectedSupplier.get();
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }
    }

}
