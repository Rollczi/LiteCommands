package dev.rollczi.litecommands.modern.command.argument.invocation.warpper.implementations;

import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapper;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueWrapperFactory;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureExpectedValueWrapperFactory implements ExpectedValueWrapperFactory {

    @Override
    public <EXPECTED> ExpectedValueWrapper<EXPECTED> wrap(Class<EXPECTED> type, Supplier<EXPECTED> value) {
        return new CompletableFutureWrapper<>(type, value);
    }

    @Override
    public Class<?> getWrapperType() {
        return CompletableFuture.class;
    }

    private static class CompletableFutureWrapper<EXPECTED> implements ExpectedValueWrapper<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final CompletableFuture<EXPECTED> completableFuture;

        private CompletableFutureWrapper(Class<EXPECTED> expectedType, Supplier<EXPECTED> completableFuture) {
            this.expectedType = expectedType;
            this.completableFuture = CompletableFuture.supplyAsync(completableFuture);
        }

        @Override
        public Object getWrappedValue() {
            return completableFuture;
        }

        @Override
        public Class<EXPECTED> getExpectedType() {
            return expectedType;
        }

    }

}
