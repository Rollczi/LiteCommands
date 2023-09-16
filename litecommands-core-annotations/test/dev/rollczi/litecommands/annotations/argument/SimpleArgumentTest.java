package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleArgumentTest {

    static class TestClass {
        void testMethod(@Arg String arg0) {}
    }

    private static SimpleArgument<Arg, String> simpleArgument;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        Method method = TestClass.class.getDeclaredMethod("testMethod", String.class);
        Parameter parameter = method.getParameters()[0];

        simpleArgument = new SimpleArgument<Arg, String>(
            AnnotationHolder.of(
                parameter.getAnnotation(Arg.class),
                WrapFormat.notWrapped(String.class),
                () -> parameter.getName()
            )
        ) {};
    }

    @Test
    void testGetName() {
        assertEquals("arg0", simpleArgument.getName());
    }

    @Test
    void testGetAnnotation() {
        assertNotNull(simpleArgument.getAnnotation());
    }

    @Test
    void testGetWrapperFormat() {
        WrapFormat<String, ?> wrapFormat = simpleArgument.getWrapperFormat();

        assertNotNull(wrapFormat);
        assertEquals(String.class, wrapFormat.getParsedType());
        assertFalse(wrapFormat.hasOutType());
    }

}