package dev.rollczi.litecommands.shared;

import dev.rollczi.litecommands.argument.Arg;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectFormatTest {

    public void testMethod(@Arg String text, @Arg int amount) {}

    @Test
    void docsMethod() throws NoSuchMethodException {
        assertEquals(
                "ReflectFormatTest#testMethod(@Arg String arg0, @Arg int arg1)",
                ReflectFormat.docsMethod(ReflectFormatTest.class.getMethod("testMethod", String.class, int.class))
        );
    }

    @Test
    void method() {
        assertEquals(
                "public void testMethod(@Arg String arg0, @Arg int arg1)",
                ReflectFormat.method(() -> ReflectFormatTest.class.getMethod("testMethod", String.class, int.class))
        );
    }

}
