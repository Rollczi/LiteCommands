package dev.rollczi.example.jda.catboy;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.jda.visibility.Visibility;
import java.util.List;
import java.util.Random;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionContextType;

@Command(name = "catboy")
@Description("Get a random catboy message!")
@Visibility(InteractionContextType.GUILD)
public class CatboyCommand {

    private static final Random RANDOM = new Random();
    private static final List<String> CATBOY_MESSAGES = List.of(
        "I love catboys!",
        "Catboys are the best!",
        "Catboys are so cute!",
        "I want to hug a catboy!",
        "Catboys are so adorable!",
        "I want to be a catboy!",
        "I want to be a catboy's friend!",
        "I want to be a catboy's boyfriend",
        "Catboys are so cool!",
        "Catboys are so handsome!"
    );

    @Execute
    void executeCatboy(
        @Context SlashCommandInteractionEvent event
    ) {
        event.reply("Getting a random catboy message...")
            .setEphemeral(true)
            .queue();

        String randomCatboyMessage = CATBOY_MESSAGES.get(RANDOM.nextInt(CATBOY_MESSAGES.size()));
        event.getChannel().sendMessage(randomCatboyMessage)
            .queue();
    }

}
