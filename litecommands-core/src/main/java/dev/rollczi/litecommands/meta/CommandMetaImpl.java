package dev.rollczi.litecommands.meta;

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

class CommandMetaImpl implements CommandMeta {

    private final Map<CommandKey<?>, Object> meta = new HashMap<>();

    public <T> CommandMeta put(CommandKey<T> key, T value) {
        this.meta.put(key, key.getType().in(value));
        return this;
    }

    public <E> CommandMeta appendToList(CommandKey<List<E>> key, E element) {
        this.addToCollection(key, element, ArrayList::new);
        return this;
    }

    public <E> CommandMeta appendToSet(CommandKey<Set<E>> key, E element) {
        this.addToCollection(key, element, HashSet::new);
        return this;
    }

    private <E, C extends Collection<E>> void addToCollection(CommandKey<C> key, E element, Supplier<C> newCollection) {
        Object objList = this.meta.getOrDefault(key, key.getDefaultValue());

        C collection = objList == null
            ? newCollection.get()
            : key.getType().in(key.getType().cast(objList));

        collection.add(element);

        this.meta.put(key, collection);
    }

    public <T> CommandMeta remove(CommandKey<T> key) {
        this.meta.remove(key);
        return this;
    }

    @Override
    public List<String> permissions() {
        return this.get(CommandMeta.PERMISSIONS);
    }

    @Override
    public List<String> permissionsExcluded() {
        return this.get(CommandMeta.PERMISSIONS_EXCLUDED);
    }

    public <T> T get(CommandKey<T> key) {
        Object value = this.meta.get(key);
        CommandMetaType<T> type = key.getType();

        if (value != null) {
            return type.out(type.cast(value));
        }

        if (key.hasDefaultValue()) {
            return type.out(key.getDefaultValue());
        }

        throw new NoSuchElementException();
    }

    @Override
    public CommandMeta clear() {
        this.meta.clear();

        return this;
    }

    @Override
    public CommandMeta apply(CommandMeta meta) {
        for (CommandKey<?> key : meta.getKeys()) {
            this.meta.put(key, meta.get(key));
        }

        return this;
    }

    @Override
    public CommandMeta copy() {
        CommandMetaImpl copy = new CommandMetaImpl();

        for (CommandKey<?> key : this.meta.keySet()) {
            copy.meta.put(key, this.getOut(key));
        }

        return copy;
    }

    private <T> T getOut(CommandKey<T> key) {
        CommandMetaType<T> type = key.getType();

        return type.out(type.cast(this.meta.get(key)));
    }

    @Override
    public Collection<CommandKey<?>> getKeys() {
        return Collections.unmodifiableSet(this.meta.keySet());
    }

}
