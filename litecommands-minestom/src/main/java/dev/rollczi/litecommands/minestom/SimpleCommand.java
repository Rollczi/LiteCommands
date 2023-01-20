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

import java.util.Arrays;
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

        ArgumentStringArray arguments = new ArgumentStringArray("[...]");
        arguments.setDefaultValue(new String[0]);
        arguments.setSuggestionCallback((sender, context, suggestionCallback) -> {
            String alias = context.getCommandName();
            String[] args = this.fixArguments(context.get(arguments));

            LiteInvocation liteInvocation = this.createInvocation(sender, alias, args);
            Set<Suggestion> suggestions = this.suggestionListener.suggest(sender, liteInvocation).suggestions();

            for (Suggestion suggestion : suggestions) {
                suggestionCallback.addEntry(new SuggestionEntry(suggestion.multilevel(), Component.empty()));
            }
        });

        this.addSyntax(((sender, context) -> {
            String alias = context.getCommandName();
            String[] args = this.fixArguments(context.get(arguments));

            this.executeListener.execute(sender, this.createInvocation(sender, alias, args));
        }), arguments);

        this.setDefaultExecutor(((sender, context) -> {
            String alias = context.getCommandName();
            this.executeListener.execute(sender, this.createInvocation(sender, alias));
        }));
    }

    private LiteInvocation createInvocation(CommandSender sender, String alias, String... args) {
        return new LiteInvocation(new MinestomSender(sender), this.commandSection.getName(), alias, args);
    }

    private String[] fixArguments(String[] args) {
        // Minestom set null character to end of array
        if (args.length > 0 && args[args.length - 1].equals("\u0000")) {
            args = Arrays.copyOf(args, args.length);

            args[args.length - 1] = "";
        }

        return args;
    }

}
