package dev.rollczi.example.jda;

import dev.rollczi.example.jda.catboy.CatboyCommand;
import dev.rollczi.example.jda.level.LevelCommand;
import dev.rollczi.example.jda.level.LevelService;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.jda.LiteJDAFactory;
import java.util.EnumSet;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class JDAExampleApplication {

    /**
     * To run this example, you need to set the DISCORD_TOKEN environment variable.
     * If you don't have a token, you can get it from the <a href="https://discord.com/developers/applications"> Discord Developer Portal</a>,
     * by creating a new application with OAuth2 bot permissions.
     */
    public static void main(String[] args) throws InterruptedException {
        String token = System.getenv("DISCORD_TOKEN");
        JDA jda = JDABuilder.createDefault(token)
            .enableIntents(EnumSet.allOf(GatewayIntent.class)) // Warning: This enables all intents!
            .build()
            .awaitReady();

        LevelService levelService = new LevelService();
        levelService.setLevel(0, "Rollczi", 10);
        levelService.setLevel(1, "vLuckyy", 5);
        levelService.setLevel(2, "Mike", 2910);

        LiteCommands<User> liteCommands = LiteJDAFactory.builder(jda)
            .settings(settings -> settings
                .guilds("896933084983877662") // <- If you want to update commands instantly, you can specify the id of your guild
            )
            .commands(
                new CatboyCommand(),
                new LevelCommand(levelService)
            )
            .build();
    }

}
