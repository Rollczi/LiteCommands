package dev.rollczi.litecommands.modern.command.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandMeta {

    public static final CommandKey<ArrayList<String>> PERMISSIONS = CommandKey.of("permissions", CommandMetaType.list(String.class));
    public static final CommandKey<ArrayList<String>> EXCLUDED_PERMISSIONS = CommandKey.of("excluded-permissions", CommandMetaType.list(String.class));
    public static final CommandKey<Boolean> ASYNCHRONOUS = CommandKey.of("asynchronous", Boolean.class, false);

    private final Map<String, Object> meta = new HashMap<>();

    public <T> void set(CommandKey<T> key, T value) {
        this.meta.put(key.getKey(), value);
    }

    public <T> T get(CommandKey<T> key) {
        return key.getType().cast(this.meta.getOrDefault(key.getKey(), key.getDefaultValue()));
    }

}
