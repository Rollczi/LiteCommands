package dev.rollczi.litecommands.modern.command.meta;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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
        return key;
    }
    
    Class<T> getType() {
        return type.getType();
    }
    
    @Nullable
    T getDefaultValue() {
        return defaultValue;
    }

    public static <T> CommandKey<T> of(String key, Class<T> type) {
        return new CommandKey<>(key, CommandMetaType.of(type), null);
    }
    
    public static <T> CommandKey<T> of(String key, Class<T> type, T defaultValue) {
        return new CommandKey<>(key, CommandMetaType.of(type), defaultValue);
    }
    
    public static <T> CommandKey<T> of(String key, CommandMetaType<T> type) {
        return new CommandKey<>(key, type, null);
    }
    
    public static <T> CommandKey<T> of(String key, CommandMetaType<T> type, T defaultValue) {
        return new CommandKey<>(key, type, defaultValue);
    }
    
}
