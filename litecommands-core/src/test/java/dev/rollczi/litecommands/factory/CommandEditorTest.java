package dev.rollczi.litecommands.factory;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.implementation.TestHandle;
import dev.rollczi.litecommands.implementation.TestPlatform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandEditorTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .command(ToEdit.class)
            .commandEditor(ToEdit.class, state -> state
                    .command("edited")
                    .aliases(Arrays.asList("alis1", "alias2"))
            )
            .register();

    @Test
    @DisplayName("test command editor for name and aliases")
    void test() {
        CommandService<TestHandle> commandService = liteCommands.getCommandService();
        CommandSection section = commandService.getSection("edited");

        assertEquals("edited", section.getName());
        assertEquals(2, section.getAliases().size());
    }

    @Section(route = "test")
    static class ToEdit {

    }

}
