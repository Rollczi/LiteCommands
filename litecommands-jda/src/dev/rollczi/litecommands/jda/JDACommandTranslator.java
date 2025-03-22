package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
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
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.shared.Preconditions;
import java.util.function.Function;
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
    private final Map<Class<?>, JDAType<String>> jdaTypeOverlays = new HashMap<>();
    private final Map<Class<?>, JDATypeWrapper<?>> jdaTypeWrappers = new HashMap<>();

    private final ParserRegistry<User> parserRegistry;

    public JDACommandTranslator(ParserRegistry<User> parserRegistry) {
        this.parserRegistry = parserRegistry;
    }

    public <T> JDACommandTranslator type(Class<T> type, OptionType optionType, JDATypeMapper<T> mapper) {
        return this.type(type, optionType, (invocation, option) -> mapper.map(option));
    }

    public <T> JDACommandTranslator type(Class<T> type, OptionType optionType, JDAContextTypeMapper<T> mapper) {
        jdaSupportedTypes.put(type, new JDAType<>(optionType, mapper));
        return this;
    }

    public <T> JDACommandTranslator typeOverlay(Class<T> type, OptionType optionType, JDATypeMapper<String> mapper) {
        jdaTypeOverlays.put(type, new JDAType<>(optionType, mapper));
        return this;
    }

    public <T> JDACommandTranslator typeOverlay(Class<T> type, OptionType optionType, JDAContextTypeMapper<String> mapper) {
        jdaTypeOverlays.put(type, new JDAType<>(optionType, mapper));
        return this;
    }

    /**
     * Wrappers are only used for wrapping types that are supported by Discord API, and we want to skip LiteCommands parsing.
     */
    public <T> JDACommandTranslator typeWrapper(Class<T> type, Function<TypeToken<T>, TypeToken<?>> unwrapper, Function<?, T> wrapper) {
        jdaTypeWrappers.put(type, new JDATypeWrapper<>(unwrapper, wrapper));
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
            JDATypeWrapper<?> wrapper = jdaTypeWrappers.get(parsedType);

            if (wrapper != null) {
                parsedType = wrapper.unwrapper(argument.getType()).getRawType();
            }

            JDAType<?> type = getType(parsedType);
            JDAContextTypeMapper<?> mapper = wrapper != null
                ? (invocation, option) -> wrapper.wrap(type.mapper().map(invocation, option))
                : type.mapper();

            consumer.translate(type.optionType(), mapper, argumentName, description, isRequired, type.optionType().canSupportChoices());
        }

        return executor;
    }

    private JDAType<?> getType(Class<?> parsedType) {
        if (jdaSupportedTypes.containsKey(parsedType)) {
            return jdaSupportedTypes.get(parsedType);
        }

        if (jdaTypeOverlays.containsKey(parsedType)) {
            return jdaTypeOverlays.get(parsedType);
        }

        return new JDAType<>(OptionType.STRING, (invocation, option) -> option.getAsString());
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
        void translate(OptionType optionType, JDAContextTypeMapper<?> mapper, String argName, String description, boolean isRequired, boolean autocomplete);
    }

    JDAParseableInput translateArguments(JDALiteCommand command, SlashCommandInteractionEvent interaction) {
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
            new JDAPlatformSender(interaction.getUser()),
            route.getName(),
            interaction.getName(),
            arguments,
            context
        );
    }

    static final class JDALiteCommand {
        private final SlashCommandData jdaCommandData;
        private final Map<JDARoute, JDAContextTypeMapper<?>> jdaArgumentTypeMappers = new HashMap<>();

        JDALiteCommand(SlashCommandData jdaCommandData) {
            this.jdaCommandData = jdaCommandData;
        }

        SlashCommandData jdaCommandData() {
            return jdaCommandData;
        }

        Object mapArgument(JDARoute jdaRoute, OptionMapping option, Invocation<?> invocation) {
            JDAContextTypeMapper<?> typeMapper = jdaArgumentTypeMappers.get(jdaRoute);

            if (typeMapper == null) {
                return null;
            }

            return typeMapper.map(invocation, option);
        }

        void addTypeMapper(JDARoute route, JDAContextTypeMapper<?> mapper) {
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

    static class JDAType<T> {
        private final OptionType optionType;
        private final JDAContextTypeMapper<T> mapper;

        JDAType(OptionType optionType, JDAContextTypeMapper<T> mapper) {
            this.optionType = optionType;
            this.mapper = mapper;
        }

        public OptionType optionType() {
            return optionType;
        }

        public JDAContextTypeMapper<T> mapper() {
            return mapper;
        }
    }

    static final class JDATypeWrapper<T> {
        private final Function<TypeToken<T>, TypeToken<?>> unwrapper;
        private final Function<Object, T> wrapper;

        public JDATypeWrapper(Function<TypeToken<T>, TypeToken<?>> unwrapper, Function<?, T> wrapper) {
            this.unwrapper = unwrapper;
            this.wrapper = (Function<Object, T>) wrapper;
        }

        public TypeToken<?> unwrapper(TypeToken<?> type) {
            return unwrapper.apply((TypeToken<T>) type);
        }

        private T wrap(Object object) {
            return wrapper.apply(object);
        }
    }

}
