package dev.rollczi.litecommands.meta;

import org.jetbrains.annotations.CheckReturnValue;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MetaSetEditor<E> {

    private final Set<E> mutableSet = new LinkedHashSet<>();
    private final Meta meta;
    private final MetaKey<Set<E>> key;

    MetaSetEditor(Set<E> set, Meta meta, MetaKey<Set<E>> key) {
        this.meta = meta;
        this.key = key;
        this.mutableSet.addAll(set);
    }

    @CheckReturnValue
    public MetaSetEditor<E> add(E element) {
        this.mutableSet.add(element);
        return this;
    }

    @CheckReturnValue
    public MetaSetEditor<E> remove(E element) {
        this.mutableSet.remove(element);
        return this;
    }

    @CheckReturnValue
    public MetaSetEditor<E> clear() {
        this.mutableSet.clear();
        return this;
    }

    @SafeVarargs
    @CheckReturnValue
    public final MetaSetEditor<E> addAll(E... value) {
        Collections.addAll(this.mutableSet, value);
        return this;
    }

    @CheckReturnValue
    public MetaSetEditor<E> addAll(Iterable<E> value) {
        value.forEach(this.mutableSet::add);
        return this;
    }

    public Meta apply() {
        return this.meta.put(this.key, this.mutableSet);
    }

}
