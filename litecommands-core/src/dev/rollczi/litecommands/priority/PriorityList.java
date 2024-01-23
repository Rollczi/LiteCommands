package dev.rollczi.litecommands.priority;

import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface PriorityList<E> extends Iterable<E> {

    @ApiStatus.Experimental
    boolean isEmpty();

    @ApiStatus.Experimental
    int size();

    @ApiStatus.Experimental
    E first();

    @ApiStatus.Experimental
    E last();

    @ApiStatus.Experimental
    Stream<E> stream();

    @ApiStatus.Experimental
    @SafeVarargs
    static <E extends Prioritized> PriorityList<E> of(E... elements) {
        MutablePriorityList<E> set = new MutablePriorityList<>();
        for (E element : elements) {
            set.add(element);
        }
        return set;
    }

}
