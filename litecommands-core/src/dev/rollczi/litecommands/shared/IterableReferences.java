package dev.rollczi.litecommands.shared;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class IterableReferences<E> implements Iterable<E> {

    private final List<Supplier<Iterable<E>>> collections;

    private IterableReferences(List<Supplier<Iterable<E>>> collections) {
        this.collections = collections;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new MergedIterator();
    }

    @SafeVarargs
    public static <E> IterableReferences<E> of(Supplier<Iterable<E>>... collections) {
        return new IterableReferences<>(Arrays.asList(collections));
    }

    private class MergedIterator implements Iterator<E> {

        private final Iterator<Supplier<Iterable<E>>> collectionIterator;

        private Iterator<E> currentIterator;

        private MergedIterator() {
            this.collectionIterator = collections.iterator();
        }

        @Override
        public boolean hasNext() {
            if (currentIterator != null && currentIterator.hasNext()) {
                return true;
            }

            while (collectionIterator.hasNext()) {
                currentIterator = collectionIterator.next().get().iterator();

                if (currentIterator.hasNext()) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public E next() {
            return currentIterator.next();
        }

    }
}
