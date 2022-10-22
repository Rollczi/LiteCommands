package dev.rollczi.litecommands.factory;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.TestHandle;
import dev.rollczi.litecommands.TestPlatform;
import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandEditorTest {

    @Test
    @DisplayName("test command editor for name and aliases (by class)")
    void testByClass() {
        TestPlatform testPlatform = new TestPlatform();
        LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
                .platform(testPlatform)
                .command(ToEdit.class)
                .commandEditor(ToEdit.class, state -> state
                        .name("edited")
                        .aliases(Arrays.asList("alis1", "alias2"))
                )
                .register();

        CommandService<TestHandle> commandService = liteCommands.getCommandService();
        CommandSection<TestHandle> section = commandService.getSection("edited");

        assertEquals("edited", section.getName());
        assertEquals(2, section.getAliases().size());
    }

    @Test
    @DisplayName("test command editor for name and aliases (by name)")
    void testByName() {
        TestPlatform testPlatform = new TestPlatform();
        LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
                .platform(testPlatform)
                .command(ToEdit.class)
                .commandEditor("test", state -> state
                        .name("edited")
                        .aliases(Arrays.asList("alis1", "alias2"))
                )
                .register();

        CommandService<TestHandle> commandService = liteCommands.getCommandService();
        CommandSection<TestHandle> section = commandService.getSection("edited");

        assertEquals("edited", section.getName());
        assertEquals(2, section.getAliases().size());
    }

    @Section(route = "test")
    static class ToEdit {

    }

}
