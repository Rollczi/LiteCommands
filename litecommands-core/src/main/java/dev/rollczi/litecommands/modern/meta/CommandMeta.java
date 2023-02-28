package dev.rollczi.litecommands.modern.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

public class CommandMeta {

    public static final CommandKey<List<String>> PERMISSIONS = CommandKey.of("permissions", CommandMetaType.list(), new ArrayList<>());
    public static final CommandKey<List<String>> EXCLUDED_PERMISSIONS = CommandKey.of("excluded-permissions", CommandMetaType.list(), new ArrayList<>());
    public static final CommandKey<Boolean> ASYNCHRONOUS = CommandKey.of("asynchronous", Boolean.class, false);

    private final Map<String, Object> meta = new HashMap<>();

    public <T> void put(CommandKey<T> key, T value) {
        this.meta.put(key.getKey(), value);
    }

    public <E> void addToList(CommandKey<List<E>> key, E element) {
        this.addToCollection(key, element, ArrayList::new);
    }

    public <E> void addToSet(CommandKey<Set<E>> key, E element) {
        this.addToCollection(key, element, HashSet::new);
    }

    private <E, C extends Collection<E>> void addToCollection(CommandKey<C> key, E element, Supplier<C> newCollection) {
        Object objList = this.meta.getOrDefault(key.getKey(), key.getDefaultValue());

        C collection = objList == null
            ? newCollection.get()
            : key.getType().cast(objList);

        collection.add(element);

        this.meta.put(key.getKey(), collection);
    }

    public <T> void remove(CommandKey<T> key) {
        this.meta.remove(key.getKey());
    }

    public <T> T get(CommandKey<T> key) {
        Object value = this.meta.get(key.getKey());

        if (value != null) {
            return key.getType().cast(value);
        }

        if (key.hasDefaultValue()) {
            return key.getDefaultValue();
        }

        throw new NoSuchElementException();
    }

}
