package dev.rollczi.litecommands.argument;

import java.util.Objects;

public class ArgumentKey {

    private static final ArgumentKey DEAFULT_UNIVERSAL = universal("");

    private final String namespace;
    private final String key;

    private ArgumentKey(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
    }

    boolean isDefault() {
        return this.key.isEmpty();
    }


    public static ArgumentKey universal(String key) {
        return ArgumentKey.key(Argument.class, key);
    }

    public static ArgumentKey universal() {
        return DEAFULT_UNIVERSAL;
    }

    public static <A extends Argument<?>> ArgumentKey key(Class<A> argumentType, String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        return new ArgumentKey(argumentType.getName(), key);
    }

    public static <A extends Argument<?>> ArgumentKey key(Class<A> argumentType) {
        return key(argumentType, "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ArgumentKey that = (ArgumentKey) o;
        return namespace.equals(that.namespace) && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

}
