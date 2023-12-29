package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

@Command(name = "test")
public class TestCommand {

    @Execute
    void test(
        @Context CommandSender sender,
        @Arg int test, @Arg Location location, @Arg String string, @Flag("-s") boolean flag, @OptionalArg String opt, @Join(limit = 5) String text
    ) {
        sender.sendMessage("Test command " + test + " " + location + " " + string + " " + flag + " " + text);
    }

}
