package dev.rollczi.litecommands.reflect;

import dev.rollczi.litecommands.reflect.type.TypeToken;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ParameterizedTypeTest {

    @Test
    void getType() {
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
        };
        Type type = typeToken.getType();

        assertEquals("java.util.List<java.lang.String>", type.getTypeName());
        assertTrue(typeToken.isInstanceOf(List.class));
        assertTrue(typeToken.isInstanceOf(Collection.class));
        assertTrue(typeToken.isInstanceOf(Object.class));
        TypeToken<?> typeToken1 = typeToken.getParameterized();
        assertEquals("java.lang.String", typeToken1.getType().getTypeName());
    }

    @Test
    void getType2() {
        TypeToken<List> typeToken = TypeToken.of(List.class);
        Type type = typeToken.getType();

        assertEquals("java.util.List", type.getTypeName());
        assertTrue(typeToken.isInstanceOf(List.class));
        assertTrue(typeToken.isInstanceOf(Collection.class));
        assertTrue(typeToken.isInstanceOf(Object.class));
    }

}