package dev.rollczi.litecommands.modern.wrapper.implementations;

import dev.rollczi.litecommands.modern.wrapper.ValueToWrap;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpected;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.modern.wrapper.WrapperFormat;
import panda.std.Option;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableFutureWrappedExpectedFactory implements WrappedExpectedFactory {

    @Override
    public <EXPECTED> WrappedExpected<EXPECTED> wrap(
        ValueToWrap<EXPECTED> valueToWrap,
        WrapperFormat<EXPECTED> info
    ) {
        Class<EXPECTED> expectedType = info.getType();

        return new CompletableFutureWrapper<>(expectedType, valueToWrap);
    }

    @Override
    public <EXPECTED> Option<WrappedExpected<EXPECTED>> empty(WrapperFormat<EXPECTED> info) {
        return Option.none();
    }

    @Override
    public Class<?> getWrapperType() {
        return CompletableFuture.class;
    }

    private static class CompletableFutureWrapper<EXPECTED> implements WrappedExpected<EXPECTED> {

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
