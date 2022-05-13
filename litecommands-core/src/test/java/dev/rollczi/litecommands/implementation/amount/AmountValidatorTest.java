package dev.rollczi.litecommands.implementation.amount;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.implementation.TestPlatform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AmountValidatorTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<Void> liteCommands = LiteFactory.builder(Void.class)
            .platform(testPlatform)
            .command(TestCommand.class)
            .resultHandler(String.class, (unused, invocation, value) -> {})
            .register();

    @Test
    void testExecuteMinOneArgument() {
        ExecuteResult one = testPlatform.execute("test");
        ExecuteResult two = testPlatform.execute("test", "siema");
        ExecuteResult three = testPlatform.execute("test", "siema", "SIEMA");

        Assertions.assertNull(one.getResult());
        Assertions.assertEquals("siema", two.getResult());
        Assertions.assertEquals("siema SIEMA", three.getResult());
    }

    @Test
    void testExecuteWithRouteAndMinTwoArguments() {
        ExecuteResult zero = testPlatform.execute("test", "key");
        ExecuteResult one = testPlatform.execute("test", "key", "siema");
        ExecuteResult two = testPlatform.execute("test", "key", "siema", "SIEMA");
        ExecuteResult three = testPlatform.execute("test", "key", "siema", "SIEMA", "witam");

        Assertions.assertEquals("key", zero.getResult());
        Assertions.assertEquals("key siema", one.getResult());
        Assertions.assertEquals("siema SIEMA", two.getResult());
        Assertions.assertEquals("siema SIEMA witam", three.getResult());
    }


}
