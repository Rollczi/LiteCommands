package dev.rollczi.litecommands.priority;

import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;

public interface PrioritizedList<E extends Prioritized> extends Iterable<E> {

    boolean isEmpty();

    int size();

    E first();

    E last();

    Stream<E> stream();

    boolean contains(E element);

    @ApiStatus.Experimental
    @SafeVarargs
    static <E extends Prioritized> PrioritizedList<E> of(E... elements) {
        MutablePrioritizedList<E> set = new MutablePrioritizedList<>();
        for (E element : elements) {
            set.add(element);
        }
        return set;
    }

}
