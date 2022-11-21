package dev.rollczi.litecommands.modern.argument.invocation.warpper;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ArgumentReturnWrapper<T> {

    Object getWrappedValue();

    class ValueWrapper<T> implements ArgumentReturnWrapper<T> {

        private final T value;

        public ValueWrapper(T value) {
            this.value = value;
        }

        @Override
        public Object getWrappedValue() {
            return value;
        }

    }

    class OptionalWrapper<T> implements ArgumentReturnWrapper<T> {

        private final T value;

        public OptionalWrapper(T value) {
            this.value = value;
        }

        @Override
        public Object getWrappedValue() {
            return Optional.ofNullable(value);
        }

    }

    class CompletedFutureWrapper<T> implements ArgumentReturnWrapper<T> {

        private final CompletableFuture<T> future;

        public CompletedFutureWrapper(CompletableFuture<T> future) {
            this.future = future;
        }

        @Override
        public Object getWrappedValue() {
            return future;
        }

    }

}
