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
    public <T> Iterable<T> iterable(MetaKey<T> key) {
        return new MetaIterable<>(key);
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
                if (current.meta().has(key)) {
                    next = current.meta().get(key);
                    current = current.parentMeta();
                    return;
                }

                current = current.parentMeta();
            }

            next = null;
        }

    }

}
