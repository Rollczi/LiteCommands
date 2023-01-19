package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;

class SimpleCommand extends Command {

    private final CommandSection<CommandSender> commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final SuggestionListener<CommandSender> suggestionListener;


    SimpleCommand(CommandSection<CommandSender> commandSection, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        super(commandSection.getName(), commandSection.getAliases().toArray(String[]::new));
        this.commandSection = commandSection;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;

        ArgumentStringArray arguments = new ArgumentStringArray("args");

        arguments.setSuggestionCallback((sender, context, suggestion) -> {
            String alias = context.getCommandName();
            String[] args = context.get("args");
            this.suggestionListener.suggest(sender, new LiteInvocation(new MinestomSender(sender), this.commandSection.getName(), alias, args)).multilevelSuggestions();
        });

        addSyntax(((sender, context) -> {
            String alias = context.getCommandName();
            String[] args = context.get(arguments);
            this.executeListener.execute(sender, new LiteInvocation(new MinestomSender(sender), this.commandSection.getName(), alias, args));
        }), arguments);

        setDefaultExecutor(((sender, context) -> {
            String alias = context.getCommandName();
            this.suggestionListener.suggest(sender, new LiteInvocation(new MinestomSender(sender), this.commandSection.getName(), alias, alias));
        }));
    }
}
