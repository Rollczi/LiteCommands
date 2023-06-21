package dev.rollczi.litecommands.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MetaListEditor<E> {

    private final List<E> mutableList = new ArrayList<>();
    private final Meta meta;
    private final MetaKey<List<E>> key;

    MetaListEditor(List<E> list, Meta meta, MetaKey<List<E>> key) {
        this.meta = meta;
        this.key = key;
        this.mutableList.addAll(list);
    }

    public MetaListEditor<E> add(E element) {
        this.mutableList.add(element);
        return this;
    }

    public MetaListEditor<E> remove(E element) {
        this.mutableList.remove(element);
        return this;
    }

    public MetaListEditor<E> clear() {
        this.mutableList.clear();
        return this;
    }

    @SafeVarargs
    public final MetaListEditor<E> addAll(E... value) {
        Collections.addAll(this.mutableList, value);
        return this;
    }

    public MetaListEditor<E> addAll(Iterable<E> value) {
        value.forEach(this.mutableList::add);
        return this;
    }

    public Meta apply() {
        return this.meta.put(this.key, this.mutableList);
    }

}
