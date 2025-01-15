package dev.rollczi.litecommands.meta;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetaTest {

    @Test
    void testSetAndGet() {
        Meta meta = Meta.create();
        MetaKey<String> key = MetaKey.of("test", String.class);
        assertThrows(NoSuchElementException.class, () -> meta.get(key));

        meta.put(key, "value");
        assertEquals("value", meta.get(key));
    }

    @Test
    void testClear() {
        Meta meta = Meta.create();
        MetaKey<String> key = MetaKey.of("test", String.class, "def");

        meta.put(key, "value");
        assertEquals("value", meta.get(key));
        meta.remove(key);
        assertEquals("def", meta.get(key));
    }

    @Test
    void testGetDefaultValue() {
        Meta meta = Meta.create();
        String text = meta.get(MetaKey.of("test", String.class, "default"));

        assertEquals("default", text);
    }

    @Test
    void testList() {
        Meta meta = Meta.create();
        LinkedHashSet<String> test = new LinkedHashSet<>();
        test.add("first");

        meta.put(Meta.PERMISSIONS, new LinkedHashSet<>(Collections.singleton(test)));
        meta.setEditor(Meta.PERMISSIONS)
            .add(Collections.singleton("second"))
            .apply();

        Set<Set<String>> set = meta.get(Meta.PERMISSIONS);
        Iterator<Set<String>> iterator = set.iterator();

        assertEquals(2, set.size());
        assertEquals(Collections.singleton("first"), iterator.next());
        assertEquals(Collections.singleton("second"), iterator.next());
    }

    @Test
    void testSet() {
        Meta meta = Meta.create();
        MetaKey<Set<String>> key = MetaKey.of("test", MetaType.set(), new HashSet<>());

        Set<String> set = meta.get(key);
        assertEquals(0, set.size());

        set.add("first");
        set.add("second");
        assertEquals(0, meta.get(key).size());

        meta.put(key, set);
        assertEquals(2, set.size());
        assertTrue(set.contains("first"));
        assertTrue(set.contains("second"));
    }

}
