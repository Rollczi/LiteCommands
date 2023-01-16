package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import dev.rollczi.litecommands.shared.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class VelocityCommand implements SimpleCommand {

    private final CommandSection<CommandSource> commandSection;
    private final ExecuteListener<CommandSource> executeListener;
    private final SuggestionListener<CommandSource> suggestionListener;
    private final boolean nativePermissions;

    public VelocityCommand(CommandSection<CommandSource> commandSection, ExecuteListener<CommandSource> executeListener, SuggestionListener<CommandSource> suggestionListener, boolean nativePermissions) {
        this.commandSection = commandSection;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
        this.nativePermissions = nativePermissions;
    }

    @Override
    public void execute(Invocation invocation) {
        this.executeListener.execute(invocation.source(), this.convert(invocation, false));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return this.suggestionListener.suggest(invocation.source(), this.convert(invocation, true)).multilevelSuggestions();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {

        if (this.nativePermissions) {
            RequiredPermissions requiredPermissions = RequiredPermissions.of(this.commandSection.meta(), new VelocitySender(invocation.source()));
            return requiredPermissions.isEmpty();
        }

        return SimpleCommand.super.hasPermission(invocation);
    }

    private LiteInvocation convert(Invocation invocation, boolean suggest) {
        List<String> arguments = new ArrayList<>(Arrays.asList(invocation.arguments()));

        if (suggest && arguments.isEmpty()) {
            arguments.add(StringUtils.EMPTY);
        }

        return new LiteInvocation(new VelocitySender(invocation.source()), this.commandSection.getName(), invocation.alias(), arguments.toArray(new String[0]));
    }

}
