package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGpt;
import org.bukkit.Bukkit;

@Command(name = "chat-gpt")
public class ChatGptCommand {

    @Execute
    void execute(@Join @ChatGpt String message) {
        Bukkit.broadcastMessage(message);
    }


}
