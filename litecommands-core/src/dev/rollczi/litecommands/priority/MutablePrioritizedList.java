package dev.rollczi.litecommands.priority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class MutablePrioritizedList<E extends Prioritized> implements PrioritizedList<E> {

    private final TreeMap<PriorityLevel, Entry<E>> priorities = new TreeMap<>(Comparator.reverseOrder());
    private final HashSet<E> elements = new HashSet<>();

    @ApiStatus.Experimental
    public MutablePrioritizedList() {
    }

    @ApiStatus.Experimental
    public MutablePrioritizedList(Iterable<E> elements) {
        for (E element : elements) {
            this.add(element);
        }
    }

    @ApiStatus.Experimental
    public void add(E element) {
        Entry<E> entry = priorities.computeIfAbsent(element.getPriority(), priority -> new Entry<>(priority));

        entry.elements.add(element);
        elements.add(element);
    }

    @ApiStatus.Experimental
    public void remove(E element) {
        Entry<E> entry = priorities.get(element.getPriority());

        if (entry != null) {
            entry.elements.remove(element);

            if (entry.elements.isEmpty()) {
                priorities.remove(element.getPriority());
            }
        }

        elements.remove(element);
    }

    @ApiStatus.Experimental
    public void clear() {
        priorities.clear();
        elements.clear();
    }

    @ApiStatus.Experimental
    @Override
    public boolean contains(E element) {
        return elements.contains(element);
    }

    @ApiStatus.Experimental
    @Override
    public boolean isEmpty() {
        return priorities.isEmpty();
    }

    @ApiStatus.Experimental
    @Override
    public int size() {
        return priorities.values().stream()
            .mapToInt(entry -> entry.elements.size())
            .sum();
    }

    @ApiStatus.Experimental
    @Override
    public E first() {
        if (this.priorities.isEmpty()) {
            throw new IllegalStateException("PrioritySet is empty");
        }

        Entry<E> entry = this.priorities.firstEntry().getValue();
        return entry.elements.get(0);
    }

    @ApiStatus.Experimental
    @Override
    public E last() {
        if (this.priorities.isEmpty()) {
            throw new IllegalStateException("PrioritySet is empty");
        }

        Entry<E> entry = this.priorities.lastEntry().getValue();
        return entry.elements.get(entry.elements.size() - 1);
    }

    @ApiStatus.Experimental
    @Override
    public Stream<E> stream() {
        return priorities.values().stream()
            .flatMap(entry -> entry.elements.stream());
    }

    @Override
    @ApiStatus.Experimental
    public @NotNull Iterator<E> iterator() {
        return priorities.values().stream()
            .flatMap(entry -> entry.elements.stream())
            .iterator();
    }

    private static class Entry<E> implements Prioritized {
        private final PriorityLevel priority;
        private final List<E> elements = new ArrayList<>();

        private Entry(PriorityLevel priority) {
            this.priority = priority;
        }

        @Override
        public PriorityLevel getPriority() {
            return priority;
        }
    }

}
