package dev.rollczi.litecommands.command.meta;

public class MetaKey<T> {

    private final String key;
    private final Class<T> type;

    private MetaKey(String key, Class<T> type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Class<T> getType() {
        return type;
    }

    static <T> MetaKey<T> of(String key, Class<T> type) {
        return new MetaKey<>(key, type);
    }

}
