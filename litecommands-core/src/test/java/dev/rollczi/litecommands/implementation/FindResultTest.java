package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import java.util.List;
import java.util.Map;

class FindResultTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .command(TestCommandLuckPermsExample.class)
            .command(TestCommandChatExample.class)
            .argument(String.class, (invocation, argument) -> Result.ok(argument))
            .register();


    @Test
    void checkNotFound() {
        FindResult findResult = testPlatform.find("lp", "user");
        List<CommandSection> sections = findResult.getSections();

        Assertions.assertEquals(sections.get(0).getName(), "lp");
        Assertions.assertEquals(sections.get(1).getName(), "user");
        Assertions.assertFalse(findResult.isFound());
    }

    @Test
    void checkFound() {
        FindResult findResult = testPlatform.find("lp", "user", "Rollczi", "parent", "set", "vip");
        List<CommandSection> sections = findResult.getSections();

        Assertions.assertEquals(sections.get(0).getName(), "lp");
        Assertions.assertEquals(sections.get(1).getName(), "user");
        Assertions.assertEquals(3, findResult.getArguments().size());
        Assertions.assertTrue(findResult.isFound());
    }

}
