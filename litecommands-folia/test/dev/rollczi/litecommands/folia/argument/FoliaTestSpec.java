package dev.rollczi.litecommands.folia.argument;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.TestUtil;
import org.bukkit.command.CommandSender;
import org.mockito.Mockito;

public class FoliaTestSpec {

    protected static Invocation<CommandSender> invocation(String command, String... args) {
        CommandSender sender = Mockito.mock(CommandSender.class);
        return TestUtil.invocation(sender, command, args);
    }

}
