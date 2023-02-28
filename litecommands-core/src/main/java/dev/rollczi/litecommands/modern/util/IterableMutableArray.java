package dev.rollczi.litecommands.modern.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@ApiStatus.Internal
public class IterableMutableArray<T> implements Iterable<T> {

    private final T[] array;

    @ApiStatus.Internal
    public IterableMutableArray(T[] array) {
        this.array = array;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {

        private int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return array.length > nextIndex;
        }

        @Override
        public T next() {
            return array[nextIndex++];
        }

    }
}
