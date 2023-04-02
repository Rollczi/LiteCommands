package dev.rollczi.litecommands.jda;


import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.platform.PlatformSuggestionHook;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.Arrays;

class JDAPlatform extends AbstractPlatform<User, LiteJDASettings> {

    private final JDA jda;

    public JDAPlatform(LiteJDASettings settings, JDA jda) {
        super(settings);
        this.jda = jda;
    }

    @Override
    protected void hook(CommandRoute<User> commandRoute, PlatformInvocationHook<User> invocationHook, PlatformSuggestionHook<User> suggestionHook) {
            jda.addEventListener(new SlashCommand());
    }

    @Override
    protected void unhook(CommandRoute<User> commandRoute) {

    }


    static class SlashCommand extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        }

        @Override
        public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {

            event.replyChoices(
                Arrays.asList(
                    new Command.Choice("user", "???"),
                    new Command.Choice("Choice 2", "choice2"),
                    new Command.Choice("Choice 3", "choice3")
                )
            ).queue();
        }

    }

}
