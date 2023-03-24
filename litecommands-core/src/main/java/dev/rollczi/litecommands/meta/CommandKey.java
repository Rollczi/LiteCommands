package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CommandKey<T> {
    
    private final String key;
    private final CommandMetaType<T> type;
    private final @Nullable T defaultValue;

    private CommandKey(String key, CommandMetaType<T> type, @Nullable T defaultValue) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    String getKey() {
        return this.key;
    }
    
    CommandMetaType<T> getType() {
        return this.type;
    }
    
    @Nullable
    T getDefaultValue() {
        return this.defaultValue;
    }

    boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public static <T> CommandKey<T> of(String key, Class<T> type) {
        return new CommandKey<>(key, CommandMetaType.of(type), null);
    }
    
    public static <T> CommandKey<T> of(String key, Class<T> type, T defaultValue) {
        return of(key, CommandMetaType.of(type), defaultValue);
    }
    
    public static <T> CommandKey<T> of(String key, CommandMetaType<T> type) {
        return of(key, type, null);
    }
    
    public static <T> CommandKey<T> of(String key, CommandMetaType<T> type, T defaultValue) {
        return new CommandKey<>(key, type, defaultValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandKey<?> that = (CommandKey<?>) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
