package dev.rollczi.litecommands.command.amount;

import dev.rollczi.litecommands.AssertResult;
import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmountValidatorTest {

    TestPlatform platform = TestFactory.withCommands(TestCommand.class);

    @Route(name = "test")
    static class TestCommand {
        @Execute(min = 1)
        public String minOneArgument(@Joiner String text) {
            return text;
        }

        @Execute(route = "key", min = 2)
        public String minTwoArgumentAndRoute(@Joiner String text) {
            return text;
        }
    }

    @Test
    @DisplayName("test min 1 argument command with 0 arguments")
    void testMinOneArgumentWithZero() {
        AssertResult result = platform.execute("test");

        result.assertNullResult();
    }

    @Test
    @DisplayName("test min 1 argument command with 1 argument")
    void testMinOneArgumentWithOne() {
        AssertResult result = platform.execute("test", "siema");

        result.assertResult("siema");
    }

    @Test
    @DisplayName("test min 1 argument command with 2 arguments")
    void testMinOneArgumentWithTwo() {
        AssertResult result = platform.execute("test", "siema", "SIEMA");

        result.assertResult("siema SIEMA");
    }

    @Test
    @DisplayName("should execute command with min 1 argument and consume `key`")
    void testExecuteWithRouteAndMinTwoArguments() {
        AssertResult one = platform.execute("test", "key");
        AssertResult two = platform.execute("test", "key", "siema");

        one.assertResult("key");
        two.assertResult("key siema");
    }

    @Test
    @DisplayName("should execute command with min 2 argument and without consume `key`")
    void testExecuteWithRouteAndMinTwoArguments2() {
        AssertResult two = platform.execute("test", "key", "siema", "SIEMA");
        AssertResult three = platform.execute("test", "key", "siema", "SIEMA", "witam");

        two.assertResult("siema SIEMA");
        three.assertResult("siema SIEMA witam");
    }

}
