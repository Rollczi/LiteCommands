package dev.rollczi.litecommands.priority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
    }

    @ApiStatus.Experimental
    public void remove(E element) {
        priorities.remove(element);
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
        return this.priorities.stream()
            .min(Comparator.comparing(Prioritized::getPriority))
            .orElseThrow(() -> new IllegalStateException("PrioritySet is empty"));
    }

    @ApiStatus.Experimental
    @Override
    public E last() {
        return this.priorities.stream()
            .max(Comparator.comparing(Prioritized::getPriority))
            .orElseThrow(() -> new IllegalStateException("PrioritySet is empty"));
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

            private final Iterator<E> iterator = priorities.stream()
                .sorted(Comparator.comparing(Prioritized::getPriority))
                .iterator();

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

    private static class Node<E extends Prioritized> implements Comparable<Node<E>> {

        private final E element;

        private Node(E element) {
            this.element = element;
        }

        @Override
        public int compareTo(@NotNull MutablePriorityList.Node<E> o) {
            return this.element.getPriority().compareTo(o.element.getPriority());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(element, node.element);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element);
        }

    }

}
