package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class VelocityCommand implements SimpleCommand {

    private final LiteVelocitySettings settings;
    private final CommandRoute<CommandSource> commandSection;
    private final PlatformInvocationListener<CommandSource> executeListener;
    private final PlatformSuggestionListener<CommandSource> suggestionListener;

    public VelocityCommand(
        LiteVelocitySettings settings,
        CommandRoute<CommandSource> command, PlatformInvocationListener<CommandSource> executeListener,
        PlatformSuggestionListener<CommandSource> suggestionListener
    ) {
        this.settings = settings;
        this.commandSection = command;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        boolean isNative = commandSection.meta().get(Meta.NATIVE_PERMISSIONS);

        if (isNative || settings.isNativePermissions()) {
            MissingPermissions missingPermissions = MissingPermissions.check(new VelocitySender(invocation.source()), this.commandSection);

            return missingPermissions.isPermitted();
        }

        return true;
    }

    @Override
    public void execute(Invocation invocation) {
        ParseableInput<?> raw = ParseableInput.raw(invocation.arguments());

        this.executeListener.execute(this.newInvocation(invocation, raw), raw);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        List<String> arguments = new ArrayList<>(Arrays.asList(invocation.arguments()));

        if (arguments.isEmpty()) {
            arguments.add(StringUtil.EMPTY);
        }

        SuggestionInput<?> input = SuggestionInput.raw(arguments.toArray(new String[0]));

        return CompletableFuture.supplyAsync(() -> this.suggestionListener.suggest(this.newInvocation(invocation, input), input)
            .asMultiLevelList());
    }

    private dev.rollczi.litecommands.invocation.Invocation<CommandSource> newInvocation(Invocation invocation, Input<?> input) {
        return new dev.rollczi.litecommands.invocation.Invocation<>(
            new VelocitySender(invocation.source()),
            this.commandSection.getName(),
            invocation.alias(),
            input
        );
    }

}
