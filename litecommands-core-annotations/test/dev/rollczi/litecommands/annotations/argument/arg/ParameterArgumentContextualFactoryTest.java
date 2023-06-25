package dev.rollczi.litecommands.annotations.argument.arg;

import dev.rollczi.litecommands.annotations.argument.ParameterArgument;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.std.OptionWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParameterArgumentContextualFactoryTest {

    static class TestClass {
        void testMethod(@Arg String arg0, @Arg int arg1) {}
        void testInvalidMethod(@Arg Option arg0) {}
    }

    private static WrapperRegistry wrapperRegistry;
    private static Method method;
    private static Parameter invalidParameter;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        wrapperRegistry = new WrapperRegistry();
        wrapperRegistry.registerFactory(new OptionWrapper());

        method = TestClass.class.getDeclaredMethod("testMethod", String.class, int.class);
        invalidParameter = TestClass.class.getDeclaredMethod("testInvalidMethod", Option.class).getParameters()[0];
    }

    @Test
    void testCreate() {
        Parameter[] parameters = method.getParameters();

        assertEquals(2, parameters.length);

        ParameterArgument<?, ?> argument1 = new ArgArgument<>(wrapperRegistry, parameters[0], parameters[0].getAnnotation(Arg.class));

        assertEquals("arg0", argument1.getName());
        assertEquals(String.class, argument1.getWrapperFormat().getParsedType());
        assertFalse(argument1.getWrapperFormat().hasOutType());
        assertEquals(Arg.class, argument1.getAnnotationType());

        ParameterArgument<?, ?> argument2 = new ArgArgument<>(wrapperRegistry, parameters[1], parameters[1].getAnnotation(Arg.class));

        assertEquals("arg1", argument2.getName());
        assertEquals(int.class, argument2.getWrapperFormat().getParsedType());
        assertFalse(argument2.getWrapperFormat().hasOutType());
        assertEquals(Arg.class, argument2.getAnnotationType());
    }

    @Test
    void testCreateInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new ParameterArgument<>(wrapperRegistry, invalidParameter, invalidParameter.getAnnotation(Arg.class)));
    }

}