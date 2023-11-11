package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

class MetaImpl implements Meta {

    private final Map<MetaKey<?>, Object> meta = new HashMap<>();

    @Override
    public <T> Meta put(MetaKey<T> key, T value) {
        this.meta.put(key, key.getType().in(value));
        return this;
    }


    public <E> Meta appendToList(MetaKey<List<E>> key, E element) {
        this.addToCollection(key, element, ArrayList::new);
        return this;
    }

    public <E> Meta appendToSet(MetaKey<Set<E>> key, E element) {
        this.addToCollection(key, element, HashSet::new);
        return this;
    }

    private <E, C extends Collection<E>> void addToCollection(MetaKey<C> key, E element, Supplier<C> newCollection) {
        Object objList = this.meta.getOrDefault(key, key.getDefaultValue());

        C collection = objList == null
            ? newCollection.get()
            : key.getType().in(key.getType().cast(objList));

        collection.add(element);

        this.meta.put(key, collection);
    }

    public <T> Meta remove(MetaKey<T> key) {
        this.meta.remove(key);
        return this;
    }

    public <T> @NotNull T get(MetaKey<T> key) {
        Object value = this.meta.get(key);
        MetaType<T> type = key.getType();

        if (value != null) {
            return type.out(type.cast(value));
        }

        if (key.hasDefaultValue()) {
            return type.out(key.getDefaultValue());
        }

        throw new NoSuchElementException();
    }

    public <T> @NotNull T get(MetaKey<T> key, T defaultValue) {
        try {
            return this.get(key);
        }
        catch (NoSuchElementException ignored) {
            return defaultValue;
        }
    }

    @Override
    public Meta clear() {
        this.meta.clear();

        return this;
    }

    @Override
    public boolean has(MetaKey<?> key) {
        return this.meta.containsKey(key);
    }

    @Override
    public Meta apply(Meta meta) {
        for (MetaKey<?> key : meta.getKeys()) {
            this.meta.put(key, meta.get(key));
        }

        return this;
    }

    @Override
    public Meta copy() {
        MetaImpl copy = new MetaImpl();

        for (MetaKey<?> key : this.meta.keySet()) {
            copy.meta.put(key, this.getOut(key));
        }

        return copy;
    }

    private <T> T getOut(MetaKey<T> key) {
        MetaType<T> type = key.getType();

        return type.out(type.cast(this.meta.get(key)));
    }

    @Override
    public Collection<MetaKey<?>> getKeys() {
        return Collections.unmodifiableSet(this.meta.keySet());
    }

}
