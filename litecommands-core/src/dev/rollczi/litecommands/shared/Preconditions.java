package dev.rollczi.litecommands.shared;


import org.jetbrains.annotations.Contract;

import java.util.Collection;

public final class Preconditions {

    private Preconditions() {
    }

    @Contract("false, _, _ -> fail")
    public static void checkArgument(boolean value, String message, Object... args) {
        if (!value)
            throw new IllegalStateException(String.format(message, args));
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

    public static void notEmpty(Object[] elements, String name) {
        if (elements.length == 0) {
            throw new IllegalArgumentException(name + " cannot be empty");
        }
    }

    public static void notContains(Collection<?> collection, Object element, String name, String elementName) {
        if (collection.contains(element)) {
            throw new IllegalArgumentException("Collection " + name + " already contains " + elementName);
        }
    }

    public static void notContains(Iterable<?> iterable, Object element, String name, String elementName) {
        for (Object obj : iterable) {
            if (obj.equals(element)) {
                throw new IllegalArgumentException("Collection " + name + " already contains " + elementName);
            }
        }
    }

    public static void isInstanceOf(Object value, Class<?> clazz, String name) {
        if (!clazz.isInstance(value)) {
            throw new IllegalArgumentException(name + " must be instance of " + clazz.getName());
        }
    }

    public static void isNotInstanceOf(Object value, Class<?> clazz, String name) {
        if (clazz.isInstance(value)) {
            throw new IllegalArgumentException(name + " cannot be instance of " + clazz.getName());
        }
    }

}
