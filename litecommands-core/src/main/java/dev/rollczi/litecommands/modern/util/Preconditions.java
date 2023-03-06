package dev.rollczi.litecommands.modern.util;

import org.jetbrains.annotations.Contract;

import java.util.Collection;

public final class Preconditions {

    private Preconditions() {
    }

    @Contract("false, _ -> fail")
    public static void checkState(boolean value, String message) {
        if (!value) throw new IllegalStateException(message);
    }

    @Contract("null, _ -> fail")
    public static void notNull(Object value, String name) {
        if (value == null) {
            throw new NullPointerException(name + " cannot be null");
        }
    }

    @Contract("_, _ -> fail")
    public static void isNull(Object value, String name) {
        if (value != null) {
            throw new IllegalArgumentException(name + " must be null");
        }
    }

    public static void notEmpty(Collection<?> value, String name) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty");
        }
    }

}
