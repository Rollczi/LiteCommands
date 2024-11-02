package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationContext;
import dev.rollczi.litecommands.jda.visibility.Visibility;
import dev.rollczi.litecommands.jda.visibility.VisibilityScope;
import dev.rollczi.litecommands.jda.permission.DiscordPermission;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.priority.PrioritizedList;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.shared.Preconditions;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JDACommandTranslator {

    private static final String DESCRIPTION_DEFAULT = "none";
    private static final String DESCRIPTION_NO_GENERATED = "no generated description";

    private final Map<Class<?>, JDAType<?>> jdaSupportedTypes = new HashMap<>();
    private final Map<Class<?>, JDATypeOverlay<?>> jdaTypeOverlays = new HashMap<>();

    private final ParserRegistry<User> parserRegistry;

    public JDACommandTranslator(ParserRegistry<User> parserRegistry) {
        this.parserRegistry = parserRegistry;
    }

    public <T> JDACommandTranslator type(Class<T> type, OptionType optionType, JDATypeMapper<T> mapper) {
        jdaSupportedTypes.put(type, new JDAType<>(type, optionType, mapper));
        return this;
    }

    public <T> JDACommandTranslator typeOverlay(Class<T> type, OptionType optionType, JDATypeMapper<String> mapper) {
        jdaTypeOverlays.put(type, new JDATypeOverlay<>(type, optionType, mapper));
        return this;
    }

    <SENDER> JDALiteCommand translate(
        CommandRoute<SENDER> commandRoute
    ) {
        CommandDataImpl commandData = new CommandDataImpl(commandRoute.getName(), this.getDescription(commandRoute));
        commandData.setGuildOnly(commandRoute.meta().get(Visibility.META_KEY) == VisibilityScope.GUILD);

        JDALiteCommand jdaLiteCommand = new JDALiteCommand(commandData);

        if (!commandRoute.getExecutors().isEmpty()) {
            if (!commandRoute.getChildren().isEmpty()) {
                throw new IllegalArgumentException("Discord command cannot have subcommands and executor in same route");
            }

            CommandExecutor<SENDER> executor = this.translateExecutor(commandRoute, (optionType, mapper, argName, description, isRequired, autocomplete) -> {
                commandData.addOption(optionType, argName, description, isRequired, autocomplete);
                jdaLiteCommand.addTypeMapper(new JDARoute(argName), mapper);
            });

            commandData.setDescription(this.getDescription(executor));

            List<Permission> permissions = getPermissions(executor);

            if (!permissions.isEmpty()) {
                commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions));
            }

            return jdaLiteCommand;
        }

        if (commandRoute.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Discord command cannot be empty");
        }

        // group command and subcommands
        for (CommandRoute<SENDER> child : commandRoute.getChildren()) {
            if (!child.getExecutors().isEmpty()) {
                SubcommandData subcommandData = new SubcommandData(child.getName(), DESCRIPTION_NO_GENERATED);
                CommandExecutor<SENDER> executor = this.translateExecutor(child, (optionType, mapper, argName, description, isRequired, autocomplete) -> {
                    subcommandData.addOption(optionType, argName, description, isRequired, autocomplete);
                    jdaLiteCommand.addTypeMapper(new JDARoute(child.getName(), argName), mapper);
                });

                subcommandData.setDescription(this.getDescription(executor));
                commandData.addSubcommands(subcommandData);
            }

            if (child.getChildren().isEmpty()) {
                continue;
            }

            SubcommandGroupData subcommandGroupData = new SubcommandGroupData(child.getName(), this.getDescription(child));

            for (CommandRoute<SENDER> childChild : child.getChildren()) {
                if (childChild.getExecutors().isEmpty()) {
                    continue;
                }

                SubcommandData subcommandData = new SubcommandData(childChild.getName(), DESCRIPTION_NO_GENERATED);
                CommandExecutor<SENDER> executor = this.translateExecutor(childChild, (optionType, mapper, argName, description, isRequired, autocomplete) -> {
                    subcommandData.addOption(optionType, argName, description, isRequired, autocomplete);
                    jdaLiteCommand.addTypeMapper(new JDARoute(child.getName(), childChild.getName(), argName), mapper);
                });

                subcommandData.setDescription(this.getDescription(executor));
                subcommandGroupData.addSubcommands(subcommandData);
            }

            commandData.addSubcommandGroups(subcommandGroupData);
        }

        List<Permission> permissions = getPermissions(commandRoute);

        if (!permissions.isEmpty()) {
            commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissions));
        }

        return jdaLiteCommand;
    }

    private String getDescription(MetaHolder holder) {
        List<List<String>> descriptions = holder.metaCollector().collect(Meta.DESCRIPTION);

        if (descriptions.isEmpty()) {
            return DESCRIPTION_DEFAULT; // Discord doesn't allow empty description
        }

        List<String> descriptionList = descriptions.get(0);
        String description = String.join(", ", descriptionList);

        if (description.isEmpty()) {
            return DESCRIPTION_DEFAULT; // Discord doesn't allow empty description
        }

        return description;
    }

    private List<Permission> getPermissions(MetaHolder holder) {
        return holder.metaCollector().collect(DiscordPermission.META_KEY).stream()
            .flatMap(permissions -> permissions.stream())
            .collect(Collectors.toList());
    }

    private <SENDER> CommandExecutor<SENDER> translateExecutor(CommandRoute<SENDER> route, TranslateExecutorConsumer consumer) {
        PrioritizedList<CommandExecutor<SENDER>> executors = route.getExecutors();
        if (executors.size() != 1) {
            throw new IllegalArgumentException("Discrod command cannot have more than one executor in same route");
        }

        CommandExecutor<SENDER> executor = executors.first();

        for (Argument<?> argument : executor.getArguments()) {
            String argumentName = argument.getName();
            String description = this.getDescription(argument);
            boolean isRequired = isRequired(argument);

            Class<?> parsedType = argument.getType().getRawType();
            if (jdaSupportedTypes.containsKey(parsedType)) {
                JDAType<?> jdaType = jdaSupportedTypes.get(parsedType);
                OptionType optionType = jdaType.optionType();

                consumer.translate(optionType, jdaType.mapper(), argumentName, description, isRequired, optionType.canSupportChoices());
                continue;
            }

            if (jdaTypeOverlays.containsKey(parsedType)) {
                JDATypeOverlay<?> jdaTypeOverlay = jdaTypeOverlays.get(parsedType);
                OptionType optionType = jdaTypeOverlay.optionType();

                consumer.translate(optionType, jdaTypeOverlay.mapper(), argumentName, description, isRequired, optionType.canSupportChoices());
                continue;
            }

            consumer.translate(OptionType.STRING, option -> option.getAsString(), argumentName, description, isRequired, true);
        }

        return executor;
    }

    private <T> boolean isRequired(Argument<T> argument) {
        if (argument.hasDefaultValue()) {
            return false;
        }

        Parser<User, T> parser = parserRegistry.getParserOrNull(argument);

        if (parser == null) {
            return true;
        }

        Range range = parser.getRange(argument);

        return range.getMin() > 0;
    }

    private interface TranslateExecutorConsumer {
        void translate(OptionType optionType, JDATypeMapper<?> mapper, String argName, String description, boolean isRequired, boolean autocomplete);
    }

    ParseableInput<?> translateArguments(JDALiteCommand command, SlashCommandInteractionEvent interaction) {
        List<String> routes = Stream.of(interaction.getSubcommandGroup(), interaction.getSubcommandName())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        Map<String, OptionMapping> options = interaction.getOptions().stream()
            .collect(Collectors.toMap(optionMapping -> optionMapping.getName(), option -> option));

        return new JDAParseableInput(routes, options, command);
    }

    JDASuggestionInput translateSuggestions(CommandAutoCompleteInteraction interaction) {
        List<String> routes = Stream.of(interaction.getSubcommandGroup(), interaction.getSubcommandName())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        Map<String, OptionMapping> options = interaction.getOptions().stream()
            .collect(Collectors.toMap(optionMapping -> optionMapping.getName(), option -> option));

        return new JDASuggestionInput(routes, options, interaction.getFocusedOption());
    }

    Invocation<User> translateInvocation(CommandRoute<User> route, Input<?> arguments, CommandInteractionPayload interaction) {
        InvocationContext context = InvocationContext.builder()
            .put(interaction)
            .put(interaction.getChannel())
            .put(interaction.getGuild())
            .put(interaction.getMember())
            .build();

        return new Invocation<>(
            interaction.getUser(),
            new JDAPlatformSender(interaction.getUser(), interaction.getMember()),
            route.getName(),
            interaction.getName(),
            arguments,
            context
        );
    }

    static final class JDALiteCommand {
        private final SlashCommandData jdaCommandData;
        private final Map<JDARoute, JDATypeMapper<?>> jdaArgumentTypeMappers = new HashMap<>();

        JDALiteCommand(SlashCommandData jdaCommandData) {
            this.jdaCommandData = jdaCommandData;
        }

        SlashCommandData jdaCommandData() {
            return jdaCommandData;
        }

        Object mapArgument(JDARoute jdaRoute, OptionMapping option) {
            JDATypeMapper<?> typeMapper = jdaArgumentTypeMappers.get(jdaRoute);

            if (typeMapper == null) {
                return null;
            }

            return typeMapper.map(option);
        }

        void addTypeMapper(JDARoute route, JDATypeMapper<?> mapper) {
            jdaArgumentTypeMappers.put(route, mapper);
        }
    }

    static class JDARoute {

        private final String subcommandGroup;
        private final String subcommandName;
        private final String argumentName;

        JDARoute(String subcommandGroup, String subcommandName, String argumentName) {
            Preconditions.notNull(subcommandGroup, "subcommandGroup");
            Preconditions.notNull(subcommandName, "subcommandName");
            Preconditions.notNull(argumentName, "argumentName");

            this.subcommandGroup = subcommandGroup;
            this.subcommandName = subcommandName;
            this.argumentName = argumentName;
        }

        JDARoute(String subcommandName, String argumentName) {
            this("", subcommandName, argumentName);
        }

        JDARoute(String argumentName) {
            this("", "", argumentName);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JDARoute jdaRoute = (JDARoute) o;
            return Objects.equals(subcommandGroup, jdaRoute.subcommandGroup) && Objects.equals(subcommandName, jdaRoute.subcommandName) && Objects.equals(argumentName, jdaRoute.argumentName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subcommandGroup, subcommandName, argumentName);
        }

    }

    static final class JDAType<T> {
        private final Class<T> type;
        private final OptionType optionType;
        private final JDATypeMapper<T> mapper;

        JDAType(Class<T> type, OptionType optionType, JDATypeMapper<T> mapper) {
            this.type = type;
            this.optionType = optionType;
            this.mapper = mapper;
        }

        public OptionType optionType() {
            return optionType;
        }

        public JDATypeMapper<T> mapper() {
            return mapper;
        }
    }

    static final class JDATypeOverlay<T> {
        private final Class<T> type;
        private final OptionType optionType;
        private final JDATypeMapper<String> mapper;

        JDATypeOverlay(Class<T> type, OptionType optionType, JDATypeMapper<String> mapper) {
            this.type = type;
            this.optionType = optionType;
            this.mapper = mapper;
        }

        public OptionType optionType() {
            return optionType;
        }

        public JDATypeMapper<String> mapper() {
            return mapper;
        }
    }

}
