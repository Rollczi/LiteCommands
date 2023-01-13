package dev.rollczi.litecommands.modern.command.contextual.warpped.implementations;

import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResult;
import dev.rollczi.litecommands.modern.command.argument.invocation.SuccessfulResult;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentFactory;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import panda.std.Option;

import java.util.function.Supplier;

public class OptionWrappedArgumentFactory implements WrappedArgumentFactory {

    @Override
    public <EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(ExpectedContextualProvider<EXPECTED> expectedContextualProvider, ExpectedContextual<EXPECTED> context) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        return new OptionWrapper<>(expectedType, expectedContextualProvider);
    }

    @Override
    public <EXPECTED> Option<WrappedArgumentWrapper<EXPECTED>> empty(ExpectedContextual<EXPECTED> context) {
        return Option.of(new OptionWrapper<>(context.getExpectedType(), () -> null));
    }

    @Override
    public Class<?> getWrapperType() {
        return Option.class;
    }

    private static class OptionWrapper<EXPECTED> implements WrappedArgumentWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final Supplier<EXPECTED> expectedSupplier;

        public OptionWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> expectedSupplier) {
            this.expectedType = expectedType;
            this.expectedSupplier = expectedSupplier;
        }

        @Override
        public Option<EXPECTED> unwrap() {
            return Option.of(expectedSupplier.get());
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }
    }

}
