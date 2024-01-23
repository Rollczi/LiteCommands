package dev.rollczi.litecommands.priority;

import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public class PrioritySet<E extends Prioritized> implements Iterable<E> {

    private final TreeSet<Node<E>> priorities = new TreeSet<>();

    @ApiStatus.Experimental
    public void add(E element) {
        priorities.add(new Node<>(element));
    }

    @ApiStatus.Experimental
    public void remove(E element) {
        priorities.remove(new Node<>(element));
    }

    @Override
    @ApiStatus.Experimental
    public @NotNull Iterator<E> iterator() {
        return new Iterator<E>() {

            private final Iterator<Node<E>> iterator = priorities.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next().element;
            }

        };
    }

    private static class Node<E extends Prioritized> implements Comparable<Node<E>> {

        private final E element;

        private Node(E element) {
            this.element = element;
        }

        @Override
        public int compareTo(@NotNull PrioritySet.Node<E> o) {
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
