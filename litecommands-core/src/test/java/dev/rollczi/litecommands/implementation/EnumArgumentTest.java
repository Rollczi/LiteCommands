package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.Section;
import org.junit.jupiter.api.Test;

class EnumArgumentTest {

    TestPlatform platform = TestFactory.create(builder -> builder
            .command(Command.class)
            .resultHandler(TestEnum.class, (testHandle, invocation, value) -> {})
    );

    @Section(route = "test")
    static class Command {
        @Execute(required = 1)
        TestEnum execute(@Arg TestEnum testEnum) { return testEnum; }
    }

    enum TestEnum { A, B }

    @Test
    void testA() {
        platform.execute("test", "A").assertResult(TestEnum.A);
    }

    @Test
    void testB() {
        platform.execute("test", "B").assertResult(TestEnum.B);
    }

    @Test
    void testFail() {
        platform.execute("test").assertFail();
        platform.execute("test", "").assertFail();
    }

    @Test
    void testSuggestion() {
        platform.suggest("test", "").assertWith("A", "B");
    }

}
