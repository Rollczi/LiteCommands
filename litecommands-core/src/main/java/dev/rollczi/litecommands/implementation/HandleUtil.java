package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.handle.LiteException;
import panda.std.Lazy;

import java.util.function.Function;
import java.util.function.Supplier;

final class HandleUtil {

    private HandleUtil() {}

    static <R> Lazy<R> handle(Supplier<R> supplier, Function<LiteException, R> handler) {
        return new Lazy<>(() -> {
            try {
                return supplier.get();
            } catch (LiteException liteException) {
                return handler.apply(liteException);
            }
        });
    }

}
