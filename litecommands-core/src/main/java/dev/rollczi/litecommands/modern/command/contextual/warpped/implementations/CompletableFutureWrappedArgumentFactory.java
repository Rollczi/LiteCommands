package dev.rollczi.litecommands.modern.command.contextual.warpped.implementations;

import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextualProvider;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentWrapper;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentFactory;
import dev.rollczi.litecommands.modern.command.contextual.ExpectedContextual;
import panda.std.Option;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureWrappedArgumentFactory implements WrappedArgumentFactory {

    @Override
    public <EXPECTED> WrappedArgumentWrapper<EXPECTED> wrap(
        ExpectedContextualProvider<EXPECTED> expectedContextualProvider,
        ExpectedContextual<EXPECTED> context
    ) {
        Class<EXPECTED> expectedType = context.getExpectedType();

        return new CompletableFutureWrapper<>(expectedType, expectedContextualProvider);
    }

    @Override
    public <EXPECTED> Option<WrappedArgumentWrapper<EXPECTED>> empty(ExpectedContextual<EXPECTED> context) {
        return Option.none();
    }

    @Override
    public Class<?> getWrapperType() {
        return CompletableFuture.class;
    }

    private static class CompletableFutureWrapper<EXPECTED> implements WrappedArgumentWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final CompletableFuture<EXPECTED> completableFuture;

        private CompletableFutureWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> completableFuture) {
            this.expectedType = expectedType;
            this.completableFuture = CompletableFuture.supplyAsync(completableFuture);
        }

        @Override
        public Object unwrap() {
            return completableFuture;
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }

    }

}
