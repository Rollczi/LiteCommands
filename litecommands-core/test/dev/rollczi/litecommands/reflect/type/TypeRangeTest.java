package dev.rollczi.litecommands.reflect.type;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

class TypeRangeTest {

    @Test
    void testDownwards() {
        TypeRange<?> range = TypeRange.downwards(LinkedList.class, Cloneable.class, Serializable.class, Object.class);

        assertTrue(range.isInRange(LinkedList.class));
        assertTrue(range.isInRange(List.class));

        assertFalse(range.isInRange(Cloneable.class));
        assertFalse(range.isInRange(Serializable.class));
        assertFalse(range.isInRange(Object.class));

        assertTrue(range.isInRange(Deque.class));
        assertTrue(range.isInRange(Queue.class));
        assertTrue(range.isInRange(Collection.class));
    }

    @Test
    void testUpwards() {
        TypeRange<?> range = TypeRange.upwards(List.class);

        assertTrue(range.isInRange(List.class));
        assertTrue(range.isInRange(LinkedList.class));
        assertFalse(range.isInRange(Collection.class));
        assertFalse(range.isInRange(Object.class));

        range = TypeRange.upwards(Collection.class);

        assertTrue(range.isInRange(List.class));
        assertTrue(range.isInRange(LinkedList.class));
        assertTrue(range.isInRange(Collection.class));
        assertFalse(range.isInRange(Object.class));
    }

    @Test
    void testSame() {
        TypeRange<?> range = TypeRange.same(List.class);

        assertTrue(range.isInRange(List.class));
        assertFalse(range.isInRange(LinkedList.class));
        assertFalse(range.isInRange(Collection.class));
        assertFalse(range.isInRange(Object.class));
    }

}