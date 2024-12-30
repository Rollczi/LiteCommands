package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.Objects;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

class JDAPlatform extends AbstractPlatform<User, LiteJDASettings> {

    private final JDA jda;
    private final Map<CommandRoute<User>, JDACommandRecord> commands = new HashMap<>();

    private static final class JDACommandRecord {
        private final JDACommandTranslator.JDALiteCommand command;
        private final PlatformInvocationListener<User> invocationHook;
        private final PlatformSuggestionListener<User> suggestionHook;

        private JDACommandRecord(
            JDACommandTranslator.JDALiteCommand command,
            PlatformInvocationListener<User> invocationHook,
            PlatformSuggestionListener<User> suggestionHook
        ) {
            this.command = command;
            this.invocationHook = invocationHook;
            this.suggestionHook = suggestionHook;
        }

        public JDACommandTranslator.JDALiteCommand command() {
            return command;
        }

        public PlatformInvocationListener<User> invocationHook() {
            return invocationHook;
        }

        public PlatformSuggestionListener<User> suggestionHook() {
            return suggestionHook;
        }
    }

    JDAPlatform(LiteJDASettings settings, JDA jda) {
        super(settings, sender -> new JDAPlatformSender(sender));
        this.jda = jda;
        this.jda.addEventListener(new SlashCommandController());
    }

    @Override
    protected void hook(CommandRoute<User> commandRoute, PlatformInvocationListener<User> invocationHook, PlatformSuggestionListener<User> suggestionHook) {
        JDACommandTranslator.JDALiteCommand translated = settings.translator().translate(commandRoute);
        List<Guild> guilds = settings.getGuilds(jda);

        for (String name : commandRoute.names()) {
            SlashCommandData command = translated.jdaCommandData().setName(name);

            if (command.isGuildOnly() && !guilds.isEmpty()) {
                for (Guild guild : guilds) {
                    guild.upsertCommand(command).queue();
                }
                continue;
            }

            this.jda.upsertCommand(command).queue();
        }

        translated.jdaCommandData().setName(commandRoute.getName());
        this.commands.put(commandRoute, new JDACommandRecord(translated, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<User> commandRoute) {
        this.commands.remove(commandRoute);
        this.jda.retrieveCommands().queue(commands -> commands.stream()
            .filter(command -> commandRoute.names().contains(command.getName()))
            .forEach(command -> this.jda.deleteCommandById(command.getIdLong()).queue())
        );

        for (Guild guild : this.settings.getGuilds(jda)) {
            guild.retrieveCommands().queue(commands -> commands.stream()
                .filter(command -> commandRoute.names().contains(command.getName()))
                .forEach(command -> guild.deleteCommandById(command.getIdLong()).queue())
            );
        }
    }

    @Override
    public void start() {
        jda.retrieveCommands().queue(commands -> commands.stream()
            .filter(command -> !commandRoutes.containsKey(command.getName()))
            .forEach(command -> jda.deleteCommandById(command.getIdLong()).queue())
        );

        for (Guild guild : settings.getGuilds(jda)) {
            guild.retrieveCommands().queue(commands -> commands.stream()
                .filter(command -> !commandRoutes.containsKey(command.getName()))
                .forEach(command -> guild.deleteCommandById(command.getIdLong()).queue())
            );
        }
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
            JDAParseableInput arguments = settings.translator().translateArguments(commandRecord.command(), event);
            Invocation<User> invocation = settings.translator().translateInvocation(commandRoute, arguments, event);

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

            JDASuggestionInput arguments = settings.translator().translateSuggestions(event);
            Invocation<User> invocation = settings.translator().translateInvocation(commandRoute, arguments, event);
            SuggestionResult result = commandRecord.suggestionHook().suggest(invocation, arguments);
            List<Command.Choice> choiceList = result.getSuggestions().stream()
                .filter(suggestion -> !suggestion.multilevel().isEmpty())
                .map(suggestion -> choice(event.getFocusedOption().getType(), suggestion))
                .filter(choice -> Objects.nonNull(choice))
                .limit(OptionData.MAX_CHOICES)
                .collect(Collectors.toList());

            event.replyChoices(choiceList).queue();
        }

        @Nullable
        private Command.Choice choice(OptionType optionType, Suggestion suggestion) {
            String multilevel = suggestion.multilevel();

            try {
                if (optionType == OptionType.INTEGER) {
                    return new Command.Choice(multilevel, Long.parseLong(multilevel));
                }

                if (optionType == OptionType.NUMBER) {
                    return new Command.Choice(multilevel, Double.parseDouble(multilevel));
                }
            }
            catch (NumberFormatException ignored) {
                return null;
            }

            return new Command.Choice(multilevel, multilevel);
        }

    }

}
