package dev.rollczi.example.bukkit.jakarta;

import dev.rollczi.example.bukkit.jakarta.command.JakartaCommand;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.jakarta.LiteJakartaExtension;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleBukkitJakartaPlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.liteCommands = LiteBukkitFactory.builder("my-jakarta-plugin")
            .extension(new LiteJakartaExtension<>())
            .argument(JakartaCommand.User.class, new JakartaCommand.UserArgumentResolver())
            .commands(
                new JakartaCommand()
            )
            .build();
    }

    @Override
    public void onDisable() {
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }
    }

}
