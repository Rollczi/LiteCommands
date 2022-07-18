package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import org.bukkit.entity.Player;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;

@Section(route = "fuckoff", aliases = "wypierdalaj")
@Permission("dev.rollczi.fuckoff")
public class FuckOffCommand {

    @Execute(min = 2)
    public void banPlayer(final Player sender, @Arg @Name("target") final Player target, @Joiner final String reason){
        target.kickPlayer(ChatUtil.color(reason));
    }

}