package dev.rollczi.litecommands.wrapper.implementations;

import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.ValueToWrap;
import dev.rollczi.litecommands.wrapper.Wrap;
import dev.rollczi.litecommands.wrapper.Wrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureWrapper implements Wrapper {

    @Override
    public <EXPECTED> Wrap<EXPECTED> create(
        ValueToWrap<EXPECTED> valueToWrap,
        WrapFormat<EXPECTED, ?> info
    ) {
        Class<EXPECTED> expectedType = info.getParsedType();

        return new CompletableFutureWrap<>(expectedType, valueToWrap);
    }

    @Override
    public Class<?> getWrapperType() {
        return CompletableFuture.class;
    }

    private static class CompletableFutureWrap<EXPECTED> implements Wrap<EXPECTED> {

        private final Class<EXPECTED> expectedType;
        private final CompletableFuture<EXPECTED> completableFuture;

        private CompletableFutureWrap(Class<EXPECTED> expectedType, Supplier<EXPECTED> completableFuture) {
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
