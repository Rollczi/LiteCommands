package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.Completer;
import dev.rollczi.litecommands.platform.ExecuteListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

class BungeeCommand extends Command implements TabExecutor {

    private final CommandSection commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final Completer<CommandSender> completer;

    public BungeeCommand(CommandSection command, ExecuteListener<CommandSender> executeListener, Completer<CommandSender> completer) {
        super(command.getName(), "", command.getAliases().toArray(new String[0]));
        this.commandSection = command;
        this.executeListener = executeListener;
        this.completer = completer;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.executeListener.execute(sender, new LiteInvocation(new BungeeSender(sender), commandSection.getName(), commandSection.getName(), args));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return completer.completion(sender, new LiteInvocation(new BungeeSender(sender), commandSection.getName(), commandSection.getName(), args)).completionsWithSpace();
    }

}
