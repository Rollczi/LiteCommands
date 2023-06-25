package dev.rollczi.litecommands.context;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ContextResult<T> {

    private final @Nullable Supplier<T> result;
    private final Object error;

    private ContextResult(@Nullable Supplier<T> result, Object error) {
        this.result = result;
        this.error = error;
    }

    public @Nullable T getResult() {
        if (result == null) {
            throw new IllegalStateException();
        }

        return result.get();
    }

    public Object getError() {
        return error;
    }

    public boolean hasError() {
        return error != null;
    }

    public boolean hasResult() {
        return result != null;
    }

    public static <T> ContextResult<T> ok(Supplier<T> supplier) {
        return new ContextResult<>(supplier, null);
    }

    public static <T> ContextResult<T> error(Object error) {
        return new ContextResult<>(null, error);
    }

}
