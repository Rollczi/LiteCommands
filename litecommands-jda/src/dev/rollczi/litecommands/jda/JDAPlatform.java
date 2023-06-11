package dev.rollczi.litecommands.jda;


import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class JDAPlatform extends AbstractPlatform<User, LiteJDASettings> {

    private final JDA jda;
    private final JDACommandTranslator translator;
    private final Map<CommandRoute<User>, JDACommandRecord> commands = new HashMap<>();

    private record JDACommandRecord(
        JDACommandTranslator.JDALiteCommand command,
        PlatformInvocationListener<User> invocationHook,
        PlatformSuggestionListener<User> suggestionHook
    ) {}

    JDAPlatform(LiteJDASettings settings, JDA jda, JDACommandTranslator translator) {
        super(settings);
        this.jda = jda;
        this.translator = translator;
        this.jda.addEventListener(new SlashCommandController());
    }

    @Override
    protected void hook(CommandRoute<User> commandRoute, PlatformInvocationListener<User> invocationHook, PlatformSuggestionListener<User> suggestionHook) {
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

            PlatformInvocationListener<User> invocationHook = commandRecord.invocationHook();
            JDAArgumentsInput arguments = translator.translateArguments(commandRecord.command(), event);
            Invocation<User> invocation = translator.translateInvocation(commandRoute, arguments, event);

            invocationHook.execute(invocation, arguments);
        }

        @Override
        public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
            CommandRoute<User> commandRoute = commandRoutes.get(event.getName());

            if (commandRoute == null) {
                event.replyChoices().queue();
                return;
            }

            JDACommandRecord commandRecord = commands.get(commandRoute);

            if (commandRecord == null) {
                event.replyChoices().queue();
                throw new IllegalStateException("Command record not found for command: " + commandRoute.getName());
            }

            JDASuggestionInput arguments = translator.translateSuggestions(event);
            Invocation<User> invocation = translator.translateInvocation(commandRoute, arguments, event);
            SuggestionResult result = commandRecord.suggestionHook().suggest(invocation, arguments);
            List<Command.Choice> choiceList = result.getSuggestions().stream()
                .map(suggestion -> new Command.Choice("", suggestion.multilevel()))
                .collect(Collectors.toList());

            event.replyChoices(choiceList).queue();
        }

    }

}
