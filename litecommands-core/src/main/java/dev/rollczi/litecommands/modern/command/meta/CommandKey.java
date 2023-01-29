package dev.rollczi.litecommands.modern.command.meta;

import org.jetbrains.annotations.Nullable;

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
    
    Class<T> getType() {
        return this.type.getType();
    }
    
    @Nullable
    T getDefaultValue() {
        return this.defaultValue;
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
