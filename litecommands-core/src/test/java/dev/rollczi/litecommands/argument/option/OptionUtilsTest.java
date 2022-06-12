package dev.rollczi.litecommands.argument.option;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import panda.std.Option;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionUtilsTest {

    @Test
    @DisplayName("test method extractOptionType()")
    void extractOptionTypeTest() throws NoSuchMethodException {
        Method method = Example.class.getDeclaredMethod("test", Option.class);
        Parameter parameter = method.getParameters()[0];
        Option<Class<?>> optionType = OptionUtils.extractOptionType(parameter);

        assertEquals(String.class, optionType.get());
    }

    private static class Example {
        void test(Option<String> option) {}
    }

}
