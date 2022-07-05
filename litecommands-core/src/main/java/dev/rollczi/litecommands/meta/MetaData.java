package dev.rollczi.litecommands.meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MetaData implements Meta {

    private final Map<MetaKey<?>, Object> meta = new HashMap<>();

    @Override
    public <T> Meta set(MetaKey<T> key, T value) {
        this.meta.put(key, value);
        return this;
    }

    @Override
    public <T> T get(MetaKey<T> key) {
        Object value = this.meta.get(key);

        if (value == null) {
            return key.getDefaultValue();
        }

        return key.getType().cast(value);
    }

    @Override
    public Map<MetaKey<?>, Object> getMeta() {
        return Collections.unmodifiableMap(this.meta);
    }

    @Override
    public Meta apply(Meta meta) {
        this.meta.putAll(meta.getMeta());
        return this;
    }

}
