package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MetaKey<T> {

    private final String key;
    private final MetaType<T> type;
    private final @Nullable T defaultValue;

    private MetaKey(String key, MetaType<T> type, @Nullable T defaultValue) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    String getKey() {
        return this.key;
    }

    MetaType<T> getType() {
        return this.type;
    }

    @Nullable
    T getDefaultValue() {
        return this.defaultValue;
    }

    boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public static <T> MetaKey<T> of(String key, Class<T> type) {
        return new MetaKey<>(key, MetaType.of(type), null);
    }

    public static <T> MetaKey<T> of(String key, Class<T> type, T defaultValue) {
        return of(key, MetaType.of(type), defaultValue);
    }

    public static <T> MetaKey<T> of(String key, MetaType<T> type) {
        return of(key, type, null);
    }

    public static <T> MetaKey<T> of(String key, MetaType<T> type, T defaultValue) {
        return new MetaKey<>(key, type, defaultValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MetaKey<?> that = (MetaKey<?>) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
