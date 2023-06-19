package dev.rollczi.litecommands.reflect;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

class ReflectFormatUtilTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface Cmd {
        String value() default "";
        Class<?>[] subCommands() default {};
        int minArgs() default 0;
    }
    @Retention(RetentionPolicy.RUNTIME)
    @interface Exe {
        Class<?>[] subCommands() default {};
    }
    @Retention(RetentionPolicy.RUNTIME)
    @interface Arg {
        int[] value() default {1, 2};
    }

    @Cmd(value = "test")
    static class TestClass {
        @Exe(subCommands = {TestClass.class, String.class})
        public void testMethod(@Arg String arg, int index) {}
    }

    @Test
    void test() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("testMethod", String.class, int.class);
        Parameter parameter = method.getParameters()[0];

        String message = ReflectFormatUtil.executablePick(method, parameter, "Can't execute method");

        assertMultiLine(message,
                "@Cmd(value = \"test\")",
                "static class TestClass {",
                "    ",
                "    @Exe(subCommands = {TestClass.class, String.class})",
                "    public void testMethod(@Arg String arg0, int arg1) {",
                "                           ^^^^^^^^^^^^^^^^ -> Can't execute method",
                "",
                "}"
        );
    }

    private void assertMultiLine(String message, String... expectedLines) {
        assertEquals(String.join("\n", expectedLines), message);
    }

}