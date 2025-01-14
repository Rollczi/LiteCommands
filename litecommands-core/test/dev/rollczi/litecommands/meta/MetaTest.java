package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.permission.PermissionSection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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
        ArrayList<PermissionSection> test = new ArrayList<>();
        test.add(PermissionSection.and("first"));

        meta.put(Meta.PERMISSIONS, test);
        meta.listEditor(Meta.PERMISSIONS)
            .add(PermissionSection.and("second"))
            .apply();

        List<String> list = meta.get(Meta.PERMISSIONS).stream()
            .flatMap(that -> that.getPermissions().stream())
            .collect(Collectors.toList());

        assertEquals(2, list.size());
        assertEquals("first", list.get(0));
        assertEquals("second", list.get(1));
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
