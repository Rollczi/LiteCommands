package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.TestHandle;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import panda.std.Blank;
import panda.std.Result;

import java.util.List;

class FindResultTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .command(TestCommandLuckPermsExample.class)
            .argument(String.class, (invocation, argument) -> Result.ok(argument))
            .register();

    @Test
    void checkNotFound() {
        FindResult<TestHandle> findResult = testPlatform.find("lp", "user");
        List<CommandSection<TestHandle>> sections = findResult.getSections();

        Assertions.assertEquals(sections.get(0).getName(), "lp");
        Assertions.assertEquals(sections.get(1).getName(), "user");
        Assertions.assertFalse(findResult.isFound());
    }

    @Test
    void checkFound() {
        FindResult<TestHandle> findResult = testPlatform.find("lp", "user", "Rollczi", "parent", "set", "vip");
        List<CommandSection<TestHandle>> sections = findResult.getSections();

        Assertions.assertEquals(sections.get(0).getName(), "lp");
        Assertions.assertEquals(sections.get(1).getName(), "user");
        Assertions.assertEquals(3, findResult.getArguments().size());
        Assertions.assertTrue(findResult.isFound());
    }

    @Section(route = "lp user", aliases = "luckperms user")
    private static class TestCommandLuckPermsExample {
        @Execute void set(@Arg String user, @Block("parent set") @Arg String rank) {}
        @Execute void unset(@Arg String user, @Block("parent unset") @Arg String rank) {}
        @Execute(required = 2) void reload(@Arg String user, @Block("reload") Blank none) {}
    }

}
