package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.ExecuteResult;
import org.junit.jupiter.api.Test;
import panda.std.Option;
import panda.std.Result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LiteCommandTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<Void> liteCommands = LiteFactory.builder(Void.class)
            .platform(testPlatform)
            .command(TestCommandLuckPermsExample.class)
            .command(TestCommandChatExample.class)
            .resultHandler(String.class, (v, invocation, value) -> {})
            .argument(String.class, (invocation, argument) -> Result.ok(argument))
            .register();

    @Test
    void testSet() {
        ExecuteResult result = testPlatform.execute("lp", "user", "Rollczi", "parent", "set", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -> vip", result.getResult());
    }

    @Test
    void testUnSet() {
        ExecuteResult result = testPlatform.execute("lp", "user", "Rollczi", "parent", "unset", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -x vip", result.getResult());
    }

    @Test
    void testSetWithAlias() {
        ExecuteResult result = testPlatform.execute("luckperms", "user", "Rollczi", "parent", "set", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -> vip", result.getResult());
    }

    @Test
    void testUnSetWithAlias() {
        ExecuteResult result = testPlatform.execute("luckperms", "user", "Rollczi", "parent", "unset", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -x vip", result.getResult());
    }

    @Test
    void testAdminChatFalse() {
        ExecuteResult result = testPlatform.execute("ac", "siema");

        assertTrue(result.isSuccess());
        assertEquals("false -> siema", result.getResult());
    }

    @Test
    void testAdminChatTrue() {
        ExecuteResult result = testPlatform.execute("ac", "-s", "siema");

        assertTrue(result.isSuccess());
        assertEquals("true -> siema", result.getResult());
    }

    @Test
    void testAdminChatKeyAndOption() {
        ExecuteResult result = testPlatform.execute("ac", "key", "siema");

        assertTrue(result.isSuccess());
        assertEquals("siema", result.getResult());

        ExecuteResult resultNull = testPlatform.execute("ac", "key");

        assertTrue(resultNull.isSuccess());
        assertEquals("null", resultNull.getResult());
    }

    @Test
    void testAdminChatTrueMore() {
        ExecuteResult result = testPlatform.execute("ac", "-s", "siema", "test", "hejo");

        assertTrue(result.isSuccess());
        assertEquals("true -> siema test hejo", result.getResult());
    }

}
