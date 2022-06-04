package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.SuggestionListener;
import dev.rollczi.litecommands.platform.ExecuteListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

class BungeeCommand extends Command implements TabExecutor {

    private final CommandSection commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final SuggestionListener<CommandSender> suggestionListener;

    public BungeeCommand(CommandSection command, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        super(command.getName(), "", command.getAliases().toArray(new String[0]));
        this.commandSection = command;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.executeListener.execute(sender, new LiteInvocation(new BungeeSender(sender), commandSection.getName(), commandSection.getName(), args));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return suggestionListener.suggest(sender, new LiteInvocation(new BungeeSender(sender), commandSection.getName(), commandSection.getName(), args)).multilevelSuggestions();
    }

}
