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

        assertEquals("string", MapUtil.findByInstanceOf(String.class, map).get());
        assertEquals(1.0, MapUtil.findByInstanceOf(Number.class, map).get());
    }

    @Test
    void testFindKeySuperTypeOf() {
        Map<Class<?>, Object> map = new HashMap<>();

        map.put(String.class, "string");
        map.put(Number.class, 1.0);

        assertEquals("string", MapUtil.findBySuperTypeOf(String.class, map).get());
        assertEquals(1.0, MapUtil.findBySuperTypeOf(Integer.class, map).get());
    }

    @Test
    void testManyTypes() {
        Map<Class<?>, Object> map = new HashMap<>();

        map.put(Throwable.class, "throwable");
        map.put(Exception.class, "exception");
        map.put(RuntimeException.class, "runtime");
        map.put(IllegalArgumentException.class, "illegal");
        map.put(IllegalStateException.class, "state");

        assertEquals("throwable", MapUtil.findBySuperTypeOf(Throwable.class, map).get());
        assertEquals("exception", MapUtil.findBySuperTypeOf(Exception.class, map).get());
        assertEquals("runtime", MapUtil.findBySuperTypeOf(RuntimeException.class, map).get());
        assertEquals("illegal", MapUtil.findBySuperTypeOf(IllegalArgumentException.class, map).get());
        assertEquals("illegal", MapUtil.findBySuperTypeOf(NumberFormatException.class, map).get());
        assertEquals("state", MapUtil.findBySuperTypeOf(IllegalStateException.class, map).get());
        assertEquals("runtime", MapUtil.findBySuperTypeOf(UnsupportedOperationException.class, map).get());
    }

}