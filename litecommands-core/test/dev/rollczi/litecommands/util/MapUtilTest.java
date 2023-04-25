package dev.rollczi.litecommands.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapUtilTest {

    @Test
    void testFindKeyInstanceOf() {
        Map<Class<?>, Object> map = new HashMap<>();

        map.put(String.class, "string");
        map.put(Integer.class, 1.0);

        assertEquals("string", MapUtil.findKeyInstanceOf(String.class, map).get());
        assertEquals(1.0, MapUtil.findKeyInstanceOf(Number.class, map).get());
    }

    @Test
    void testFindKeySuperTypeOf() {
        Map<Class<?>, Object> map = new HashMap<>();

        map.put(String.class, "string");
        map.put(Number.class, 1.0);

        assertEquals("string", MapUtil.findKeySuperTypeOf(String.class, map).get());
        assertEquals(1.0, MapUtil.findKeySuperTypeOf(Integer.class, map).get());
    }

}