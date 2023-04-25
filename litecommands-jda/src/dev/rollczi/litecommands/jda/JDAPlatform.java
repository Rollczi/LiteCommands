package dev.rollczi.litecommands.jda;


import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.platform.PlatformSuggestionHook;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class JDAPlatform extends AbstractPlatform<User, LiteJDASettings> {

    private final JDA jda;
    private final Map<CommandRoute<User>, PlatformInvocationHook<User>> invocationHooks = new HashMap<>();
    private final Map<CommandRoute<User>, PlatformSuggestionHook<User>> suggestionHooks = new HashMap<>();

    public JDAPlatform(LiteJDASettings settings, JDA jda) {
        super(settings);
        this.jda = jda;
        this.jda.addEventListener(new SlashCommandController());
    }

    @Override
    protected void hook(CommandRoute<User> commandRoute, PlatformInvocationHook<User> invocationHook, PlatformSuggestionHook<User> suggestionHook) {
        this.invocationHooks.put(commandRoute, invocationHook);
        this.suggestionHooks.put(commandRoute, suggestionHook);

        SlashCommandData commandData = new CommandDataImpl(commandRoute.getName(), "Command description")
            .addOption(OptionType.STRING, "test", "Test option", true);


                this.jda.upsertCommand(commandData)
    }

    @Override
    protected void unhook(CommandRoute<User> commandRoute) {
        this.invocationHooks.remove(commandRoute);
        this.suggestionHooks.remove(commandRoute);
    }


    class SlashCommandController extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            CommandRoute<User> commandRoute = commandRoutes.get(event.getName());

            if (commandRoute == null) {
                event.reply("Command not found").setEphemeral(true).queue();
                return;
            }

            PlatformInvocationHook<User> invocationHook = invocationHooks.get(commandRoute);

            if (invocationHook == null) {
                event.reply("Command hook not found").setEphemeral(true).queue();
                return;
            }

            SlashCommandInteraction interaction = event.getInteraction();

            InputArguments.NamedBuilder builder = InputArguments.namedBuilder();

            if (interaction.getSubcommandName() != null) {
                builder.literal(interaction.getSubcommandName());
            }

            if (interaction.getSubcommandGroup() != null) {
                builder.literal(interaction.getSubcommandGroup());
            }

            interaction.getOptions().forEach(option -> {
                builder.namedArgument(option.getName(), option.getAsString());
            });



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
