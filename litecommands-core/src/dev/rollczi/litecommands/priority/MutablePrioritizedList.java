package dev.rollczi.litecommands.priority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class MutablePrioritizedList<E extends Prioritized> implements PrioritizedList<E> {

    private final TreeMap<PriorityLevel, Entry<E>> priorities = new TreeMap<>(Comparator.reverseOrder());
    private final HashSet<E> elements = new HashSet<>();

    public MutablePrioritizedList() {
    }

    public MutablePrioritizedList(Iterable<E> elements) {
        for (E element : elements) {
            this.add(element);
        }
    }

    public void add(E element) {
        Entry<E> entry = priorities.computeIfAbsent(element.getPriority(), priority -> new Entry<>(priority));

        entry.elements.add(element);
        elements.add(element);
    }

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

    public void clear() {
        priorities.clear();
        elements.clear();
    }

    @Override
    public boolean contains(E element) {
        return elements.contains(element);
    }

    @Override
    public boolean isEmpty() {
        return priorities.isEmpty();
    }

    @Override
    public int size() {
        return priorities.values().stream()
            .mapToInt(entry -> entry.elements.size())
            .sum();
    }

    @Override
    public E first() {
        if (this.priorities.isEmpty()) {
            throw new IllegalStateException("PrioritySet is empty");
        }

        Entry<E> entry = this.priorities.firstEntry().getValue();
        return entry.elements.get(0);
    }

    @Override
    public E last() {
        if (this.priorities.isEmpty()) {
            throw new IllegalStateException("PrioritySet is empty");
        }

        Entry<E> entry = this.priorities.lastEntry().getValue();
        return entry.elements.get(entry.elements.size() - 1);
    }

    @Override
    public Stream<E> stream() {
        return priorities.values().stream()
            .flatMap(entry -> entry.elements.stream());
    }

    @Override
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
