package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;

class MergeCommandClassesTest {

    TestPlatform platform = TestFactory.withCommands(FirstCommandClass.class, SecondCommandClass.class);

    @Section(route = "test")
    static class FirstCommandClass {
        @Execute(route = "set") public static String set(@Arg String name, @Arg String value) {
            return name + " -> " + value;
        }
    }

    @Section(route = "test")
    static class SecondCommandClass {
        @Execute(route = "unset") public static String get(@Arg String name) {
            return name + " value";
        }
    }

    @Test
    void test() {
        platform.execute("test", "unset", "Rollczi").assertResult("Rollczi value");
        platform.execute("test", "set", "Rollczi", "vip").assertResult("Rollczi -> vip");
    }

}
