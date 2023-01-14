package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class SimpleCommand extends org.bukkit.command.Command {

    private final CommandSection<CommandSender> commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final SuggestionListener<CommandSender> suggestionListener;


    SimpleCommand(CommandSection<CommandSender> commandSection, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        super(commandSection.getName(), "", "/" + commandSection.getName(), new ArrayList<>(commandSection.getAliases()));
        this.commandSection = commandSection;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        this.executeListener.execute(sender, new LiteInvocation(new BukkitSender(sender), this.commandSection.getName(), alias, args));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        return this.suggestionListener.suggest(sender, new LiteInvocation(new BukkitSender(sender), this.commandSection.getName(), alias, args)).multilevelSuggestions();
    }

}
