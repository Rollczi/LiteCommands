package dev.rollczi.litecommands.argument.enumeration;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

class EnumArgumentTest {

    TestPlatform platform = TestFactory.create(builder -> builder
            .command(Command.class)
            .resultHandler(TestEnum.class, (testHandle, invocation, value) -> {})
    );

    @Route(name = "test")
    static class Command {
        @Execute(required = 1)
        TestEnum execute(@Arg TestEnum testEnum) { return testEnum; }

        @Execute(route = "empty")
        void executeEmpty(@Arg EmptyEnum emptyEnum) {}
    }

    enum TestEnum { A, B }

    enum EmptyEnum {}

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
        platform.suggestAsOp("test", "").assertWith("A", "B", "empty");
    }

    @Test
    void testSuggestionWithContent() {
        platform.suggestAsOp("test", "A").assertWith("A");
    }

    @Test
    void testEmptyEnum() {
        platform.execute("test", "empty").assertFail();
        platform.suggestAsOp("test", "empty", "")
            .assertWith();
    }

}
