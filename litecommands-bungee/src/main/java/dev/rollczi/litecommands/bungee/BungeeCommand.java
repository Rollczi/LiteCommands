package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

class BungeeCommand extends Command implements TabExecutor {

    private final CommandSection<CommandSender> commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final SuggestionListener<CommandSender> suggestionListener;
    private final boolean nativePermissions;

    public BungeeCommand(CommandSection<CommandSender> command, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener, boolean nativePermissions) {
        super(command.getName(), "", command.getAliases().toArray(new String[0]));
        this.commandSection = command;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
        this.nativePermissions = nativePermissions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.executeListener.execute(sender, new LiteInvocation(new BungeeSender(sender), this.commandSection.getName(), this.commandSection.getName(), args));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return this.suggestionListener.suggest(sender, new LiteInvocation(new BungeeSender(sender), this.commandSection.getName(), this.commandSection.getName(), args)).multilevelSuggestions();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {

        if (this.nativePermissions) {
            RequiredPermissions requiredPermissions = RequiredPermissions.of(this.commandSection.meta(), new BungeeSender(sender));
            return requiredPermissions.isEmpty();
        }

        return super.hasPermission(sender);
    }
}
