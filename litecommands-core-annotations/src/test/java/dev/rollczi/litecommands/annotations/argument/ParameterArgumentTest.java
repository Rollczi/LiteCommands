package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.wrapper.WrapperFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParameterArgumentTest {

    static class TestClass {
        void testMethod(@Arg String arg0) {}
    }

    private static ParameterArgument<Arg, String> parameterArgument;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        Method method = TestClass.class.getDeclaredMethod("testMethod", String.class);
        Parameter parameter = method.getParameters()[0];

        parameterArgument = new ParameterArgument<>(
            method,
            parameter,
            0,
            parameter.getAnnotation(Arg.class),
            Arg.class,
            WrapperFormat.notWrapped(String.class)
        );
    }

    @Test
    void testGetName() {
        assertEquals("@Arg String arg0", parameterArgument.getName());
    }

    @Test
    void testGetAnnotation() {
        assertNotNull(parameterArgument.getAnnotation());
    }

    @Test
    void testGetAnnotationType() {
        assertEquals(Arg.class, parameterArgument.getAnnotationType());
    }

    @Test
    void testGetWrapperFormat() {
        WrapperFormat<String, ?> wrapperFormat = parameterArgument.getWrapperFormat();

        assertNotNull(wrapperFormat);
        assertEquals(String.class, wrapperFormat.getType());
        assertFalse(wrapperFormat.hasWrapper());
    }

    @Test
    void testGetParameter() {
        assertNotNull(parameterArgument.getParameter());
    }

    @Test
    void testGetParameterIndex() {
        assertEquals(0, parameterArgument.getParameterIndex());
    }

}