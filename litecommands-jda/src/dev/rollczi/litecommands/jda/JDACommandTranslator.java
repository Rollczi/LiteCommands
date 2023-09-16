package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.requirement.ArgumentRequirement;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationContext;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.shared.Preconditions;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
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

class JDACommandTranslator {

    private final Map<Class<?>, JDAType<?>> jdaSupportedTypes = new HashMap<>();
    private final Map<Class<?>, JDATypeOverlay<?>> jdaTypeOverlays = new HashMap<>();

    <T> JDACommandTranslator type(Class<T> type, OptionType optionType, JDATypeMapper<T> mapper) {
        jdaSupportedTypes.put(type, new JDAType<>(type, optionType, mapper));
        return this;
    }

    <T> JDACommandTranslator typeOverlay(Class<T> type, OptionType optionType, JDATypeMapper<String> mapper) {
        jdaTypeOverlays.put(type, new JDATypeOverlay<>(type, optionType, mapper));
        return this;
    }

    <SENDER> JDALiteCommand translate(
        String name,
        CommandRoute<SENDER> commandRoute
    ) {
        CommandDataImpl commandData = new CommandDataImpl(name, commandRoute.meta().get(Meta.DESCRIPTION));
        commandData.setGuildOnly(true);
        JDALiteCommand jdaLiteCommand = new JDALiteCommand(commandData);

        // Single command
        if (!commandRoute.getExecutors().isEmpty()) {
            this.translateExecutor(commandRoute, (optionType, mapper, argName, description, isRequired, autocomplete) -> {
                commandData.addOption(optionType, argName, description, isRequired, autocomplete);
                jdaLiteCommand.addTypeMapper(new JDARoute(argName), mapper);
            });

            return jdaLiteCommand;
        }

        if (commandRoute.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Discord command cannot be empty");
        }

        // group command and subcommands
        for (CommandRoute<SENDER> child : commandRoute.getChildren()) {
            if (!child.getExecutors().isEmpty()) {
                SubcommandData subcommandData = new SubcommandData(child.getName(), child.meta().get(Meta.DESCRIPTION));

                this.translateExecutor(child, (optionType, mapper, argName, description, isRequired, autocomplete) -> {
                    subcommandData.addOption(optionType, argName, description, isRequired, autocomplete);
                    jdaLiteCommand.addTypeMapper(new JDARoute(child.getName(), argName), mapper);
                });

                commandData.addSubcommands(subcommandData);
            }

            if (child.getChildren().isEmpty()) {
                continue;
            }

            SubcommandGroupData subcommandGroupData = new SubcommandGroupData(child.getName(), child.meta().get(Meta.DESCRIPTION));

            for (CommandRoute<SENDER> childChild : child.getChildren()) {
                if (childChild.getExecutors().isEmpty()) {
                    continue;
                }

                SubcommandData subcommandData = new SubcommandData(childChild.getName(), childChild.meta().get(Meta.DESCRIPTION));

                this.translateExecutor(childChild, (optionType, mapper, argName, description, isRequired, autocomplete) -> {
                    subcommandData.addOption(optionType, argName, description, isRequired, autocomplete);
                    jdaLiteCommand.addTypeMapper(new JDARoute(child.getName(), childChild.getName(), argName), mapper);
                });

                subcommandGroupData.addSubcommands(subcommandData);
            }

            commandData.addSubcommandGroups(subcommandGroupData);
        }

        return jdaLiteCommand;
    }

    private <SENDER> void translateExecutor(CommandRoute<SENDER> route, TranslateExecutorConsumer consumer) {
        List<CommandExecutor<SENDER, ?>> executors = route.getExecutors();
        if (executors.size() != 1) {
            throw new IllegalArgumentException("Discrod command cannot have more than one executor in same route");
        }

        CommandExecutor<SENDER, ?> executor = executors.get(0);

        for (Requirement<SENDER, ?> requirement : executor.getRequirements()) {
            if (!(requirement instanceof ArgumentRequirement argumentRequirement)) {
                continue;
            }

            Argument<?> argument = argumentRequirement.getArgument();
            String argumentName = argument.getName();
            String description = /*argument.getDescription();*/ "test"; //TODO: Add description to Argument
            boolean isRequired = !argumentRequirement.isWrapperOptional();

            Class<?> parsedType = argument.getWrapperFormat().getParsedType();
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
    }

    private interface TranslateExecutorConsumer {
        void translate(OptionType optionType, JDATypeMapper<?> mapper, String argName, String description, boolean isRequired, boolean autocomplete);
    }

    JDAParseableInput translateArguments(JDALiteCommand command, SlashCommandInteractionEvent interaction) {
        List<String> routes = Stream.of(interaction.getSubcommandGroup(), interaction.getSubcommandName())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        Map<String, OptionMapping> options = interaction.getOptions().stream()
            .collect(Collectors.toMap(OptionMapping::getName, option -> option));

        return new JDAParseableInput(routes, options, command);
    }

    JDASuggestionInput translateSuggestions(CommandAutoCompleteInteraction interaction) {
        List<String> routes = Stream.of(interaction.getSubcommandGroup(), interaction.getSubcommandName())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        Map<String, OptionMapping> options = interaction.getOptions().stream()
            .collect(Collectors.toMap(OptionMapping::getName, option -> option));

        return new JDASuggestionInput(routes, options, interaction.getFocusedOption());
    }

    Invocation<User> translateInvocation(CommandRoute<User> route, Input<?> arguments, CommandInteractionPayload interaction) {
        InvocationContext context = InvocationContext.builder()
            .putUnsafe(interaction.getClass(), interaction)
            .put(MessageChannelUnion.class, (MessageChannelUnion) interaction.getChannel())
            .put(Guild.class, interaction.getGuild())
            .put(Member.class, interaction.getMember())
            .build();

        return new Invocation<>(
            interaction.getUser(),
            new JDAPlatformSender(interaction.getUser()),
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
            if (!(o instanceof JDARoute jdaRoute)) return false;
            return Objects.equals(subcommandGroup, jdaRoute.subcommandGroup) && Objects.equals(subcommandName, jdaRoute.subcommandName) && Objects.equals(argumentName, jdaRoute.argumentName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subcommandGroup, subcommandName, argumentName);
        }
    }

    record JDAType<T>(Class<T> type, OptionType optionType, JDATypeMapper<T> mapper) {}

    record JDATypeOverlay<T>(Class<T> type, OptionType optionType, JDATypeMapper<String> mapper) {}

    interface JDATypeMapper<T> {
        T map(OptionMapping option);
    }

}
