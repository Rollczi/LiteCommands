package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.Completer;
import dev.rollczi.litecommands.platform.ExecuteListener;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

class SimpleCommand extends org.bukkit.command.Command {

    private final CommandSection commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final Completer<CommandSender> completer;

    public SimpleCommand(CommandSection commandSection, ExecuteListener<CommandSender> executeListener, Completer<CommandSender> completer) {
        super(commandSection.getName(), "", "/" + commandSection.getName(), new ArrayList<>(commandSection.getAliases()));
        this.commandSection = commandSection;
        this.executeListener = executeListener;
        this.completer = completer;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        this.executeListener.execute(sender, new LiteInvocation(new BukkitSender(sender), commandSection.getName(), alias, args));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return this.completer.completion(sender, new LiteInvocation(new BukkitSender(sender), commandSection.getName(), alias, args)).completionsWithSpace();
    }

}
