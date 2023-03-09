package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.annotation.contextual.ParameterContextual;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.modern.wrapper.implementations.OptionWrappedExpectedFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

class ParameterArgumentContextualCreatorTest {

    static class TestClass {
        void testMethod(@Arg String arg0, @Arg int arg1) {}
        void testInvalidMethod(@Arg Option arg0) {}
    }

    private static ParameterArgumentContextualCreator creator;
    private static Method method;
    private static Parameter invalidParameter;

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        WrappedExpectedService wrappedExpectedService = new WrappedExpectedService();
        wrappedExpectedService.registerFactory(new OptionWrappedExpectedFactory());

        creator = new ParameterArgumentContextualCreator(wrappedExpectedService);
        method = TestClass.class.getDeclaredMethod("testMethod", String.class, int.class);
        invalidParameter = TestClass.class.getDeclaredMethod("testInvalidMethod", Option.class).getParameters()[0];
    }

    @Test
    void testCreate() {
        Parameter[] parameters = method.getParameters();

        assertEquals(2, parameters.length);

        ParameterContextual<?> firstContextual = creator.apply(parameters[0]).get(0);
        ParameterArgument<Arg, String> parameterArgument = assertInstanceOf(ParameterArgument.class, firstContextual);

        assertEquals("@Arg String arg0", parameterArgument.getName());
        assertEquals(String.class, parameterArgument.getWrapperFormat().getType());
        assertEquals(Void.class, parameterArgument.getWrapperFormat().getWrapperType());
        assertEquals(Arg.class, parameterArgument.getAnnotationType());

        ParameterContextual<?> secondContextual = creator.apply(parameters[1]).get(0);
        ParameterArgument<Arg, Integer> parameterArgument2 = assertInstanceOf(ParameterArgument.class, secondContextual);

        assertEquals("@Arg int arg1", parameterArgument2.getName());
        assertEquals(int.class, parameterArgument2.getWrapperFormat().getType());
        assertEquals(Void.class, parameterArgument2.getWrapperFormat().getWrapperType());
        assertEquals(Arg.class, parameterArgument2.getAnnotationType());
    }

    @Test
    void testCreateInvalid() {
        assertThrows(IllegalArgumentException.class, () -> creator.apply(invalidParameter));
    }

}