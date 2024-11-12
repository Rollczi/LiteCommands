package dev.rollczi.litecommands.shared;

@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> {

    void run() throws E;
}