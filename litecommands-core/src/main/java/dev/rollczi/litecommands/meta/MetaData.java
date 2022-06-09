package dev.rollczi.litecommands.meta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MetaData implements Meta {

    private final Map<MetaKey<?>, Object> meta = new HashMap<>();

    @Override
    public <T> void set(MetaKey<T> key, T value) {
        this.meta.put(key, value);
    }

    @Override
    public <T> T get(MetaKey<T> key) {
        return key.getType().cast(this.meta.get(key));
    }

    @Override
    public Map<MetaKey<?>, Object> getMeta() {
        return Collections.unmodifiableMap(this.meta);
    }

    @Override
    public void apply(Meta meta) {
        this.meta.putAll(meta.getMeta());
    }

}
