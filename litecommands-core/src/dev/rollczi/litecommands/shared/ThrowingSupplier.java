package dev.rollczi.litecommands.shared;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> {

    T get() throws E;

}