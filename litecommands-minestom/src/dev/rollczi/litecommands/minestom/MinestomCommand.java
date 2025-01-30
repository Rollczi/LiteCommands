package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

import java.util.Arrays;
import java.util.Set;

class MinestomCommand extends Command {

    private final MinestomPlatform platform;
    private final CommandRoute<CommandSender> command;
    private final PlatformInvocationListener<CommandSender> invocationHook;
    private final PlatformSuggestionListener<CommandSender> suggestionListener;

    MinestomCommand(MinestomPlatform platform, CommandRoute<CommandSender> command, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionListener) {
        super(command.getName(), command.getAliases().toArray(new String[0]));
        this.platform = platform;
        this.command = command;
        this.invocationHook = invocationHook;
        this.suggestionListener = suggestionListener;

        ArgumentStringArray arguments = new ArgumentStringArray("[...]");
        arguments.setDefaultValue(new String[0]);
        arguments.setSuggestionCallback((sender, context, suggestionCallback) -> {
            String alias = context.getCommandName();
            String[] args = this.fixArguments(context.get(arguments));

            SuggestionInput<?> input = SuggestionInput.raw(args);
            Invocation<CommandSender> invocation = this.createInvocation(sender, alias, input);
            Set<Suggestion> suggestions = this.suggestionListener.suggest(invocation, input).getSuggestions();

            if (suggestions.isEmpty()) {
                suggestionCallback.addEntry(new SuggestionEntry(StringUtil.EMPTY));
                return;
            }

            for (Suggestion suggestion : suggestions) {
                suggestionCallback.addEntry(new SuggestionEntry(suggestion.multilevel(), tooltip(suggestion.tooltip())));// todo ComponentSerializer?
            }
        });

        this.addSyntax(((sender, context) -> {
            String alias = context.getCommandName();
            String[] args = this.fixArguments(context.get(arguments));
            ParseableInput<?> raw = ParseableInput.raw(args);

            this.invocationHook.execute(this.createInvocation(sender, alias, raw), raw);
        }), arguments);

        this.setDefaultExecutor(((sender, context) -> {
            String alias = context.getCommandName();
            ParseableInput<?> raw = ParseableInput.raw();

            this.invocationHook.execute(this.createInvocation(sender, alias, raw), raw);
        }));
    }

    Component tooltip(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return Component.text(string);
    }

    private Invocation<CommandSender> createInvocation(CommandSender sender, String alias, Input<?> input) {
        MinestomSender minestomSender = new MinestomSender(sender);
        minestomSender.setPlatform(platform);
        return new Invocation<>(sender, minestomSender, this.command.getName(), alias, input);
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
