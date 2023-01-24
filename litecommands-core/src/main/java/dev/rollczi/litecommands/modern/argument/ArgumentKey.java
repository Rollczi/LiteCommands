package dev.rollczi.litecommands.modern.argument;

import java.util.Objects;

public class ArgumentKey {

    public static final ArgumentKey DEFAULT = new ArgumentKey("");

    private final String key;

    private ArgumentKey(String key) {
        this.key = key;
    }

    public static ArgumentKey of(String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        return new ArgumentKey(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArgumentKey)) {
            return false;
        }
        ArgumentKey that = (ArgumentKey) o;
        return this.key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }

}
