package dev.rollczi.example.jda.level;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.jda.visibility.Visibility;
import dev.rollczi.litecommands.jda.visibility.VisibilityScope;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Command(name = "level")
@Visibility(VisibilityScope.GUILD)
public class LevelCommand {

    private final LevelService levelService;

    public LevelCommand(LevelService levelService) {
        this.levelService = levelService;
    }

    @Execute(name = "set")
    void setLevel(@Context SlashCommandInteractionEvent event,
        @Arg int level,
        @Arg Member member
    ) {
        event.reply("Setting level to " + level + " for " + member.getAsMention())
            .setEphemeral(true)
            .queue();

        levelService.setLevel(member.getIdLong(), member.getUser().getName(), level);
    }

    @Execute(name = "clear")
    void clearLevel(@Context SlashCommandInteractionEvent event, @Arg Member member) {
        event.reply("Clearing level for " + member.getAsMention())
            .setEphemeral(true)
            .queue();

        levelService.setLevel(member.getIdLong(), member.getUser().getName(), 0);
    }

    @Execute(name = "show")
    void showLevel(@Context SlashCommandInteractionEvent event, @Arg Member member) {
        event.reply("Member " + member.getAsMention() + " has level " + levelService.getLevel(member))
            .setEphemeral(true)
            .queue();
    }

    @Execute(name = "top")
    void showTop(@Context SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        String top = levelService.getTopLevels(10).stream()
            .map(level -> level.nickname() + " - " + level.level())
            .collect(Collectors.joining("\n"));

        EmbedBuilder builder = new EmbedBuilder()
            .setTimestamp(ZonedDateTime.now())
            .setColor(0xbb33aa)
            .setAuthor("LiteCommands | Rollczi", "https://github.com/Rollczi/LiteCommands", guild.getIconUrl())
            .setThumbnail(guild.getIconUrl())
            .setTitle("Top levels in " + guild.getName())
            .addField(":trophy: Top", top, false)
            .setFooter("Requested by " + event.getUser().getAsTag(), event.getUser().getAvatarUrl());

        event.replyEmbeds(builder.build())
            .setEphemeral(true)
            .queue();
    }

}
