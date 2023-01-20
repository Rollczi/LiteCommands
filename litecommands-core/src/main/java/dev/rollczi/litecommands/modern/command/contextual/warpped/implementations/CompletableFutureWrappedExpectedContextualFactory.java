package dev.rollczi.litecommands.modern.command.contextual.warpped.implementations;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextual;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualFactory;
import panda.std.Option;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureWrappedExpectedContextualFactory implements WrappedExpectedContextualFactory {

    @Override
    public <EXPECTED> WrappedExpectedContextual<EXPECTED> wrap(
        ExpectedContextualProvider<EXPECTED> expectedContextualProvider,
        ExpectedContextual<EXPECTED> context
    ) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        return new CompletableFutureWrapper<>(expectedType, expectedContextualProvider);
    }

    @Override
    public <EXPECTED> Option<WrappedExpectedContextual<EXPECTED>> empty(ExpectedContextual<EXPECTED> context) {
        return Option.none();
    }

    @Override
    public Class<?> getWrapperType() {
        return CompletableFuture.class;
    }

    private static class CompletableFutureWrapper<EXPECTED> implements WrappedExpectedContextual<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final CompletableFuture<EXPECTED> completableFuture;

        private CompletableFutureWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> completableFuture) {
            this.expectedType = expectedType;
            this.completableFuture = CompletableFuture.supplyAsync(completableFuture);
        }

        @Override
        public Object unwrap() {
            return this.completableFuture;
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return this.expectedType;
        }

    }

}
