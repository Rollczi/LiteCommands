package dev.rollczi.example.bukkit.adventure;

import dev.rollczi.example.bukkit.adventure.command.KeyCommand;
import dev.rollczi.example.bukkit.adventure.command.NoticeCommand;
import dev.rollczi.example.bukkit.adventure.command.TeleportCommand;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.bukkit.platform.LiteAdventurePlatformExtension;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleAdventurePlugin extends JavaPlugin {

    private AudienceProvider audienceProvider;
    private MiniMessage miniMessage;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.audienceProvider = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.miniMessage();

        this.liteCommands = LiteCommandsBukkit.builder("my-plugin")

            // register additional Kyori Adventure features
            // more: https://docs.advntr.dev/minimessage/format.html
            .extension(new LiteAdventurePlatformExtension<>(this.audienceProvider), configuration -> configuration
                .miniMessage(true) // (<red>, <gradient:red:blue>, <#ff0000>, etc.)
                .legacyColor(true) // (&c, &a, etc.)
                .colorizeArgument(true) // colorize (@Arg Component)
                .serializer(this.miniMessage) // custom serializer
            )

            .commands(
                new TeleportCommand(),
                new KeyCommand(),
                new NoticeCommand(audienceProvider)
            )

            .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> "<gradient:red:blue>Player " + input + "not found!")
            .message(LiteBukkitMessages.PLAYER_ONLY, "<red>Only player can execute this command!")
            .message(LiteBukkitMessages.LOCATION_INVALID_FORMAT, input -> "<red>Invalid location format '" + input + "'! Use: <x> <y> <z>")
            .message(LiteBukkitMessages.WORLD_NOT_EXIST, input -> "<red>World " + input + " doesn't exist!")

            .build();
    }

    @Override
    public void onDisable() {
        // unregister all commands from bukkit
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }    
        if (this.audienceProvider != null) {
            this.audienceProvider.close();
        }    
    }

}
