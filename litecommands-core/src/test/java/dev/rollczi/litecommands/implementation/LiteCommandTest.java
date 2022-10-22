package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.TestFactory;
import dev.rollczi.litecommands.TestPlatform;
import org.junit.jupiter.api.Test;

class LiteCommandTest {

    TestPlatform platform = TestFactory.withCommands(TestCommandLuckPermsExample.class, TestCommandChatExample.class);

    @Test
    void testSet() {
        platform.execute("lp", "user", "Rollczi", "parent", "set", "vip")
                .assertSuccess()
                .assertResult("Rollczi -> vip");
    }

    @Test
    void testUnSet() {
        platform.execute("lp", "user", "Rollczi", "parent", "unset", "vip")
                .assertSuccess()
                .assertResult("Rollczi -x vip");
    }

    @Test
    void testSetWithAlias() {
        platform.execute("luckperms", "user", "Rollczi", "parent", "set", "vip")
                .assertSuccess()
                .assertResult("Rollczi -> vip");
    }

    @Test
    void testUnSetWithAlias() {
        platform.execute("luckperms", "user", "Rollczi", "parent", "unset", "vip")
                .assertSuccess()
                .assertResult("Rollczi -x vip");
    }

    @Test
    void testReload() {
        platform.execute("luckperms", "user", "Rollczi", "reload")
                .assertSuccess()
                .assertResult("Rollczi -reload");
    }

    @Test
    void testAdminChatFalse() {
        platform.execute("ac", "siema")
                .assertSuccess()
                .assertResult("false -> siema");
    }

    @Test
    void testAdminChatTrue() {
        platform.execute("ac", "-s", "siema")
                .assertSuccess()
                .assertResult("true -> siema");
    }

    @Test
    void testAdminChatKeyAndOption() {
        platform.execute("ac", "key", "siema")
                .assertSuccess()
                .assertResult("siema");

        platform.execute("ac", "key")
                .assertSuccess()
                .assertResult("null");
    }

    @Test
    void testAdminChatTrueMore() {
        platform.execute("ac", "-s", "siema", "test", "hejo")
                .assertSuccess()
                .assertResult("true -> siema test hejo");
    }

}
