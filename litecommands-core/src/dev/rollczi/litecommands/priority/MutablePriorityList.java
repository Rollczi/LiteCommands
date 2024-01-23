package dev.rollczi.litecommands.priority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public class MutablePriorityList<E extends Prioritized> implements PriorityList<E> {

    private final List<E> priorities = new ArrayList<>();

    @ApiStatus.Experimental
    public MutablePriorityList() {
    }

    @ApiStatus.Experimental
    public MutablePriorityList(Collection<E> elements) {
        this.priorities.addAll(elements);
    }

    @ApiStatus.Experimental
    public MutablePriorityList(PriorityList<E> priorityList) {
        this.priorities.addAll(priorityList.stream().collect(Collectors.toList()));
    }

    @ApiStatus.Experimental
    public void add(E element) {
        priorities.add(element);
        priorities.sort(Comparator.comparing(Prioritized::getPriority));
    }

    @ApiStatus.Experimental
    public void remove(E element) {
        priorities.remove(element);
        priorities.sort(Comparator.comparing(Prioritized::getPriority));
    }

    @ApiStatus.Experimental
    @Override
    public boolean isEmpty() {
        return priorities.isEmpty();
    }

    @ApiStatus.Experimental
    @Override
    public int size() {
        return priorities.size();
    }

    @ApiStatus.Experimental
    @Override
    public E first() {
        if (this.priorities.isEmpty()) {
            throw new IllegalStateException("PrioritySet is empty");
        }

        return this.priorities.get(0);
    }

    @ApiStatus.Experimental
    @Override
    public E last() {
        if (this.priorities.isEmpty()) {
            throw new IllegalStateException("PrioritySet is empty");
        }

        return this.priorities.get(this.priorities.size() - 1);
    }

    @ApiStatus.Experimental
    @Override
    public Stream<E> stream() {
        return priorities.stream()
            .sorted(Comparator.comparing(Prioritized::getPriority));
    }

    @Override
    @ApiStatus.Experimental
    public @NotNull Iterator<E> iterator() {
        return new Iterator<E>() {

            private final Iterator<E> iterator = priorities.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

        };
    }

    @Override
    @ApiStatus.Experimental
    public @NotNull ListIterator<E> listIterator() {
        return new ListIterator<E>() {

            private final ListIterator<E> iterator = priorities.listIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public boolean hasPrevious() {
                return iterator.hasPrevious();
            }

            @Override
            public E previous() {
                return iterator.previous();
            }

            @Override
            public int nextIndex() {
                return iterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iterator.previousIndex();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("PrioritySet iterator is immutable");
            }

            @Override
            public void set(E e) {
                throw new UnsupportedOperationException("PrioritySet iterator is immutable");
            }

            @Override
            public void add(E e) {
                throw new UnsupportedOperationException("PrioritySet iterator is immutable");
            }

        };
    }

}
