package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.input.SuggestionInput;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

import java.util.Arrays;
import java.util.Set;

class MinestomCommand extends Command {

    private final CommandRoute<CommandSender> command;
    private final PlatformInvocationListener<CommandSender> invocationHook;
    private final PlatformSuggestionListener<CommandSender> suggestionListener;

    MinestomCommand(CommandRoute<CommandSender> command, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionListener) {
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
            Set<Suggestion> suggestions = this.suggestionListener.suggest(invocation, SuggestionInput.raw(args)).getSuggestions();

            for (Suggestion suggestion : suggestions) {
                suggestionCallback.addEntry(new SuggestionEntry(suggestion.from(), Component.empty()));
            }
        });

        this.addSyntax(((sender, context) -> {
            String alias = context.getCommandName();
            String[] args = this.fixArguments(context.get(arguments));

            this.invocationHook.execute(this.createInvocation(sender, alias, args), InputArguments.raw(args));
        }), arguments);

        this.setDefaultExecutor(((sender, context) -> {
            String alias = context.getCommandName();
            this.invocationHook.execute(this.createInvocation(sender, alias), InputArguments.raw());
        }));
    }

    private Invocation<CommandSender> createInvocation(CommandSender sender, String alias, String... args) {
        return new Invocation<>(sender, new MinestomSender(sender), this.command.getName(), alias, InputArguments.raw(args));
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
