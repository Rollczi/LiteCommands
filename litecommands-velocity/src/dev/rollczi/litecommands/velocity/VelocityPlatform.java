package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.platform.PlatformSuggestionHook;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class VelocityPlatform extends AbstractPlatform<CommandSource, LiteVelocitySettings> {

    private final CommandManager commandManager;

    public VelocityPlatform(CommandManager commandManager, LiteVelocitySettings settings) {
        super(settings);
        this.commandManager = commandManager;
    }


    @Override
    protected void hook(CommandRoute<CommandSource> commandRoute, PlatformInvocationHook<CommandSource> invocationHook, PlatformSuggestionHook<CommandSource> suggestionHook) {
        VelocityCommand velocityCommand = new VelocityCommand(commandRoute, invocationHook, suggestionHook);

        CommandMeta meta = commandManager.metaBuilder(commandRoute.getName())
            .aliases(commandRoute.getAliases().toArray(new String[0]))
            .build();

        this.commandManager.register(meta, velocityCommand);
    }

    @Override
    protected void unhook(CommandRoute<CommandSource> commandRoute) {
        for (String name : commandRoute.names()) {
            this.commandManager.unregister(name);
        }
    }

    private class VelocityCommand implements SimpleCommand {

        private final CommandRoute<CommandSource> commandSection;
        private final PlatformInvocationHook<CommandSource> executeListener;
        private final PlatformSuggestionHook<CommandSource> suggestionListener;

        public VelocityCommand(CommandRoute<CommandSource> command, PlatformInvocationHook<CommandSource> executeListener, PlatformSuggestionHook<CommandSource> suggestionListener) {
            this.commandSection = command;
            this.executeListener = executeListener;
            this.suggestionListener = suggestionListener;
        }

        @Override
        public boolean hasPermission(Invocation invocation) {
            boolean isNative = commandSection.meta().get(dev.rollczi.litecommands.meta.CommandMeta.NATIVE_PERMISSIONS);

            if (isNative || liteConfiguration.isNativePermissions()) {
                MissingPermissions missingPermissions = MissingPermissions.check(new VelocitySender(invocation.source()), this.commandSection);

                return missingPermissions.isPermitted();
            }

            return true;
        }

        @Override
        public void execute(Invocation invocation) {
            this.executeListener.execute(this.newInvocation(invocation, false));
        }

        @Override
        public List<String> suggest(Invocation invocation) {
            return this.suggestionListener.suggest(this.newInvocation(invocation, true))
                .getSuggestions()
                .stream()
                .map(Suggestion::multilevel)
                .collect(Collectors.toList());
        }

        // TODO suggestAsync

        private dev.rollczi.litecommands.invocation.Invocation<CommandSource> newInvocation(Invocation invocation, boolean suggest) {
            List<String> arguments = new ArrayList<>(Arrays.asList(invocation.arguments()));

            if (suggest && arguments.isEmpty()) {
                arguments.add(StringUtils.EMPTY);
            }

            return new dev.rollczi.litecommands.invocation.Invocation<>(
                invocation.source(),
                new VelocitySender(invocation.source()),
                this.commandSection.getName(),
                invocation.alias(),
                InputArguments.raw(arguments)
            );
        }

    }

}
