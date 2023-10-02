package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.command.BanCommand;
import dev.rollczi.example.bukkit.command.ChatGptCommand;
import dev.rollczi.litecommands.chatgpt.ChatGptModel;
import dev.rollczi.litecommands.chatgpt.LiteChatGptExtension;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class ExampleChatGptPlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.liteCommands = LiteCommandsBukkit.builder("my-chatgpt-plugin")
            .extension(new LiteChatGptExtension<>(settings -> settings
                .apiKey("OPENAI_API_KEY") // get your api key from https://platform.openai.com/account/api-keys
                .model(ChatGptModel.GPT_4) // get model from https://platform.openai.com/docs/models/gpt-3-5
                .temperature(0.7)
                .tokensLimit(1, 64) // min and max tokens
                .cooldown(Duration.ofMillis(600)) // cooldown between suggestions per player
            ))

            .commands(LiteCommandsAnnotations.of(
                new ChatGptCommand(),
                new BanCommand()
            ))

            .build();
    }

    @Override
    public void onDisable() {
        this.liteCommands.unregister();
    }

}
