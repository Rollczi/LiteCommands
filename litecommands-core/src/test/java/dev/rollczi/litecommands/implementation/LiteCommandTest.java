package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LiteCommandTest {

    TestPlatform platform = TestFactory.withCommands(TestCommandLuckPermsExample.class, TestCommandChatExample.class);

    @Test
    void testSet() {
        ExecuteResult result = platform.executeLegacy("lp", "user", "Rollczi", "parent", "set", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -> vip", result.getResult());
    }

    @Test
    void testUnSet() {
        ExecuteResult result = platform.executeLegacy("lp", "user", "Rollczi", "parent", "unset", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -x vip", result.getResult());
    }

    @Test
    void testSetWithAlias() {
        ExecuteResult result = platform.executeLegacy("luckperms", "user", "Rollczi", "parent", "set", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -> vip", result.getResult());
    }

    @Test
    void testUnSetWithAlias() {
        ExecuteResult result = platform.executeLegacy("luckperms", "user", "Rollczi", "parent", "unset", "vip");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -x vip", result.getResult());
    }

    @Test
    void testReload() {
        ExecuteResult result = platform.executeLegacy("luckperms", "user", "Rollczi", "reload");

        assertTrue(result.isSuccess());
        assertEquals("Rollczi -reload", result.getResult());
    }

    @Test
    void testAdminChatFalse() {
        ExecuteResult result = platform.executeLegacy("ac", "siema");

        assertTrue(result.isSuccess());
        assertEquals("false -> siema", result.getResult());
    }

    @Test
    void testAdminChatTrue() {
        ExecuteResult result = platform.executeLegacy("ac", "-s", "siema");

        assertTrue(result.isSuccess());
        assertEquals("true -> siema", result.getResult());
    }

    @Test
    void testAdminChatKeyAndOption() {
        ExecuteResult result = platform.executeLegacy("ac", "key", "siema");

        assertTrue(result.isSuccess());
        assertEquals("siema", result.getResult());

        ExecuteResult resultNull = platform.executeLegacy("ac", "key");

        assertTrue(resultNull.isSuccess());
        assertEquals("null", resultNull.getResult());
    }

    @Test
    void testAdminChatTrueMore() {
        ExecuteResult result = platform.executeLegacy("ac", "-s", "siema", "test", "hejo");

        assertTrue(result.isSuccess());
        assertEquals("true -> siema test hejo", result.getResult());
    }

}
