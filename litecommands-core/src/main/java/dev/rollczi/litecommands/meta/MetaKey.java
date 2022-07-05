package dev.rollczi.litecommands.meta;

public class MetaKey<T> {

    private final String key;
    private final Class<T> type;
    private final T defaultValue;

    private MetaKey(String key, Class<T> type, T defaultValue) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public Class<T> getType() {
        return type;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public static <T> MetaKey<T> of(String key, Class<T> type) {
        return new MetaKey<>(key, type, null);
    }

    public static <T> MetaKey<T> of(String key, Class<T> type, T defaultValue) {
        return new MetaKey<>(key, type, defaultValue);
    }

}
