package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.platform.PlatformSuggestionHook;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

import java.util.Arrays;
import java.util.List;

class SimpleCommand extends Command {

    private final CommandRoute<CommandSender> command;
    private final PlatformInvocationHook<CommandSender> invocationHook;
    private final PlatformSuggestionHook<CommandSender> suggestionListener;

    SimpleCommand(CommandRoute<CommandSender> command, PlatformInvocationHook<CommandSender> invocationHook, PlatformSuggestionHook<CommandSender> suggestionListener) {
        super(command.getName(), command.getAliases().toArray(new String[0]));
        this.command = command;
        this.invocationHook = invocationHook;
        this.suggestionListener = suggestionListener;

        ArgumentStringArray arguments = new ArgumentStringArray("[...]");
        arguments.setDefaultValue(new String[0]);
        arguments.setSuggestionCallback((sender, context, suggestionCallback) -> {
            String alias = context.getCommandName();
            String[] args = this.fixArguments(context.get(arguments));

            Invocation<CommandSender> invocation = this.createInvocation(sender, alias, args);
            List<Suggestion> suggestions = this.suggestionListener.suggest(invocation).getSuggestions();

            for (Suggestion suggestion : suggestions) {
                suggestionCallback.addEntry(new SuggestionEntry(suggestion.multilevel(), Component.empty()));
            }
        });

        this.addSyntax(((sender, context) -> {
            String alias = context.getCommandName();
            String[] args = this.fixArguments(context.get(arguments));

            this.invocationHook.execute(this.createInvocation(sender, alias, args));
        }), arguments);

        this.setDefaultExecutor(((sender, context) -> {
            String alias = context.getCommandName();
            this.invocationHook.execute(this.createInvocation(sender, alias));
        }));
    }

    private Invocation<CommandSender> createInvocation(CommandSender sender, String alias, String... args) {
        return new Invocation<>(sender, new MinestomSender(sender), this.command.getName(), alias, arguments);
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
