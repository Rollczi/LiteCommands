package dev.rollczi.example.bukkit.adventure.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;

@Command(name = "notice", aliases = "n")
@Permission("dev.rollczi.notice")
public class NoticeCommand {

    private final AudienceProvider audienceProvider;

    public NoticeCommand(AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
    }

    @Execute(name = "message")
    public void message(@Arg Component component) {
        this.audienceProvider.all().sendMessage(component);
    }

    @Execute(name = "actionbar")
    public void actionbar(@Arg Component component) {
        this.audienceProvider.all().sendActionBar(component);
    }

    @Execute(name = "title")
    public void title(@Arg Component component) {
        this.audienceProvider.all().sendTitlePart(TitlePart.TITLE, component);
    }

    @Execute(name = "subtitle")
    public void subtitle(@Arg Component component) {
        this.audienceProvider.all().sendTitlePart(TitlePart.SUBTITLE, component);
    }

}
