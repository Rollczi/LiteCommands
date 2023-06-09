package dev.rollczi.litecommands.jda;


import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
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
import java.util.HashMap;
import java.util.Map;

class JDAPlatform extends AbstractPlatform<User, LiteJDASettings> {

    private final JDA jda;
    private final JDACommandTranslator translator;
    private final Map<CommandRoute<User>, JDACommandRecord> commands = new HashMap<>();

    private record JDACommandRecord(
        JDACommandTranslator.JDALiteCommand command,
        PlatformInvocationHook<User> invocationHook,
        PlatformSuggestionHook<User> suggestionHook
    ) {}

    JDAPlatform(LiteJDASettings settings, JDA jda, JDACommandTranslator translator) {
        super(settings);
        this.jda = jda;
        this.translator = translator;
        this.jda.addEventListener(new SlashCommandController());
    }

    @Override
    protected void hook(CommandRoute<User> commandRoute, PlatformInvocationHook<User> invocationHook, PlatformSuggestionHook<User> suggestionHook) {
        JDACommandTranslator.JDALiteCommand translated = translator.translate(commandRoute.getName(), commandRoute);

        for (String name : commandRoute.names()) {
            this.jda.upsertCommand(translated.jdaCommandData().setName(name))
                .queue();
        }

        translated.jdaCommandData().setName(commandRoute.getName());
        this.commands.put(commandRoute, new JDACommandRecord(translated, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<User> commandRoute) {
        this.commands.remove(commandRoute);
    }

    class SlashCommandController extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            CommandRoute<User> commandRoute = commandRoutes.get(event.getName());

            if (commandRoute == null) {
                event.reply("Command not found").setEphemeral(true).queue();
                return;
            }

            JDACommandRecord commandRecord = commands.get(commandRoute);

            if (commandRecord == null) {
                event.reply("Command record not found").setEphemeral(true).queue();
                throw new IllegalStateException("Command record not found for command: " + commandRoute.getName());
            }

            PlatformInvocationHook<User> invocationHook = commandRecord.invocationHook();
            Invocation<User> invocation = translator.translate(commandRoute, commandRecord.command(), event);

            invocationHook.execute(invocation);
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
