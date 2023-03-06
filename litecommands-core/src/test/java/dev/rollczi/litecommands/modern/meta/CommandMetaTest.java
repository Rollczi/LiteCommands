package dev.rollczi.litecommands.modern.meta;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandMetaTest {

    @Test
    void testSetAndGet() {
        CommandMeta meta = CommandMeta.create();
        CommandKey<String> key = CommandKey.of("test", String.class);
        assertThrows(NoSuchElementException.class, () -> meta.get(key));

        meta.put(key, "value");
        assertEquals("value", meta.get(key));
    }

    @Test
    void testClear() {
        CommandMeta meta = CommandMeta.create();
        CommandKey<String> key = CommandKey.of("test", String.class, "def");

        meta.put(key, "value");
        assertEquals("value", meta.get(key));
        meta.remove(key);
        assertEquals("def", meta.get(key));
    }

    @Test
    void testGetDefaultValue() {
        CommandMeta meta = CommandMeta.create();
        String text = meta.get(CommandKey.of("test", String.class, "default"));

        assertEquals("default", text);
    }

    @Test
    void testList() {
        CommandMeta meta = CommandMeta.create();
        ArrayList<String> test = new ArrayList<>();
        test.add("first");

        meta.put(CommandMeta.PERMISSIONS, test);
        meta.appendToList(CommandMeta.PERMISSIONS, "second");

        List<String> list = meta.get(CommandMeta.PERMISSIONS);

        assertEquals(2, list.size());
        assertEquals("first", list.get(0));
        assertEquals("second", list.get(1));
    }

    @Test
    void testSet() {
        CommandMeta meta = CommandMeta.create();
        CommandKey<Set<String>> key = CommandKey.of("test", CommandMetaType.set(), new HashSet<>());

        meta.appendToSet(key, "first");
        meta.appendToSet(key, "second");

        Set<String> set = meta.get(key);

        assertEquals(2, set.size());
        assertTrue(set.contains("first"));
        assertTrue(set.contains("second"));
    }

}
