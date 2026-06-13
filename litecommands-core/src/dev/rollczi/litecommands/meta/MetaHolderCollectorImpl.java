package dev.rollczi.litecommands.meta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class MetaHolderCollectorImpl implements MetaCollector {

    private final MetaHolder metaHolder;

    MetaHolderCollectorImpl(MetaHolder metaHolder) {
        this.metaHolder = metaHolder;
    }

    @Override
    public <T> List<T> collect(MetaKey<T> key) {
        List<T> collected = new ArrayList<>();
        MetaHolder current = metaHolder;

        while (current != null) {
            if (current.meta().has(key)) {
                collected.add(current.meta().get(key));
            }

            current = current.parentMeta();
        }

        return collected;
    }

    @Override
    public <T> List<T> collectReverse(MetaKey<T> key) {
        List<T> collected = new ArrayList<>();
        MetaHolder current = metaHolder;

        while (current != null) {
            if (current.meta().has(key)) {
                collected.add(0, current.meta().get(key));
            }

            current = current.parentMeta();
        }

        return collected;
    }

    @Override
    public <T> Iterable<T> iterable(MetaKey<T> key) {
        return new MetaIterable<>(key);
    }

    @Override
    public <T> T findFirst(MetaKey<T> key) {
        T first = this.findFirst(key, null);

        if (first == null) {
            T defaultValue = key.getDefaultValue();

            if (defaultValue == null) {
                throw new NoSuchElementException("Meta value with key " + key.getKey() + " not found!");
            }

            return defaultValue;
        }

        return first;
    }

    @Override
    public <T> T findFirst(MetaKey<T> key, T defaultValue) {
        MetaHolder current = metaHolder;

        while (current != null) {
            if (current.meta().has(key)) {
                return current.meta().get(key);
            }

            current = current.parentMeta();
        }

        return defaultValue;
    }

    @Override
    public <T> T findLast(MetaKey<T> key) {
        T last = this.findLast(key, null);

        if (last == null) {
            T defaultValue = key.getDefaultValue();

            if (defaultValue == null) {
                throw new NoSuchElementException("Meta value with key " + key.getKey() + " not found!");
            }

            return defaultValue;
        }

        return last;
    }

    @Override
    public <T> T findLast(MetaKey<T> key, T defaultValue) {
        MetaHolder current = metaHolder;
        T last = null;

        while (current != null) {
            if (current.meta().has(key)) {
                last = current.meta().get(key);
            }

            current = current.parentMeta();
        }

        return last == null ? defaultValue : last;
    }

    private class MetaIterable<T> implements Iterable<T> {

        private final MetaKey<T> key;

        private MetaIterable(MetaKey<T> key) {
            this.key = key;
        }

        @Override
        public Iterator<T> iterator() {
            return new MetaIterator<>(key);
        }

    }

    private class MetaIterator<T> implements Iterator<T> {

        private final MetaKey<T> key;
        private MetaHolder current = metaHolder;
        private T next;

        private MetaIterator(MetaKey<T> key) {
            this.key = key;
            findNext();
        }


        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements to iterate!");
            }

            T next = this.next;
            findNext();
            return next;
        }

        private void findNext() {
            while (current != null) {
                T t = current.meta().get(key, null);
                if (t != null) {
                    next = t;
                    current = current.parentMeta();
                    return;
                }

                current = current.parentMeta();
            }

            next = null;
        }

    }

}
