package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.unit.Invocations;
import org.bukkit.command.CommandSender;
import org.mockito.Mockito;

public class BukkitTestSpec {

    protected static Invocation<CommandSender> invocation(String command, String... args) {
        CommandSender sender = Mockito.mock(CommandSender.class);
        return Invocations.invocation(sender, command, args);
    }

}
