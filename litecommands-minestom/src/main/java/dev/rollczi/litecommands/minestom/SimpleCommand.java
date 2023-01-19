package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

import java.util.Set;

class SimpleCommand extends Command {

    private final CommandSection<CommandSender> commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final SuggestionListener<CommandSender> suggestionListener;


    SimpleCommand(CommandSection<CommandSender> commandSection, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        super(commandSection.getName(), commandSection.getAliases().toArray(new String[0]));
        this.commandSection = commandSection;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;

        ArgumentStringArray arguments = new ArgumentStringArray("args");

        arguments.setSuggestionCallback((sender, context, suggestionCallback) -> {
            String alias = context.getCommandName();
            String[] args = context.get(arguments);
            LiteInvocation liteInvocation = new LiteInvocation(new MinestomSender(sender), this.commandSection.getName(), alias, args);
            Set<Suggestion> suggestions = this.suggestionListener.suggest(sender, liteInvocation).suggestions();
            for (Suggestion suggestion : suggestions) {
                suggestionCallback.addEntry(new SuggestionEntry(suggestion.multilevel(), Component.empty()));
            }
        });

        addSyntax(((sender, context) -> {
            String alias = context.getCommandName();
            String[] args = context.get(arguments);
            this.executeListener.execute(sender, new LiteInvocation(new MinestomSender(sender), this.commandSection.getName(), alias, args));
        }), arguments);

        setDefaultExecutor(((sender, context) -> {
            String alias = context.getCommandName();
            this.executeListener.execute(sender, new LiteInvocation(new MinestomSender(sender), this.commandSection.getName(), alias, alias));
        }));
    }
}
