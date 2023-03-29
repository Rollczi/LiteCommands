package dev.rollczi.litecommands.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandMetaListEditor<E> {

    private final List<E> mutableList = new ArrayList<>();
    private final CommandMeta meta;
    private final CommandKey<List<E>> key;

    CommandMetaListEditor(List<E> list, CommandMeta meta, CommandKey<List<E>> key) {
        this.meta = meta;
        this.key = key;
        this.mutableList.addAll(list);
    }

    public CommandMetaListEditor<E> add(E element) {
        this.mutableList.add(element);
        return this;
    }

    public CommandMetaListEditor<E> remove(E element) {
        this.mutableList.remove(element);
        return this;
    }

    public CommandMetaListEditor<E> clear() {
        this.mutableList.clear();
        return this;
    }

    @SafeVarargs
    public final CommandMetaListEditor<E> addAll(E... value) {
        Collections.addAll(this.mutableList, value);
        return this;
    }

    public CommandMetaListEditor<E> addAll(Iterable<E> value) {
        value.forEach(this.mutableList::add);
        return this;
    }

    public CommandMeta apply() {
        return this.meta.put(this.key, this.mutableList);
    }

}
