package dev.rollczi.litecommands.sponge.internal;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.sponge.LiteSpongeSettings;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.api.command.CommandCause;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ApiStatus.Internal
public class LiteSpongeCommand {

    private final CommandRoute<CommandCause> commandSection;
    private final PlatformInvocationListener<CommandCause> executeListener;
    private final PlatformSuggestionListener<CommandCause> suggestionListener;

    public LiteSpongeCommand(CommandRoute<CommandCause> commandSection, PlatformInvocationListener<CommandCause> executeListener, PlatformSuggestionListener<CommandCause> suggestionListener) {
        this.commandSection = commandSection;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
    }

    public String getLabel() {
        return commandSection.getName();
    }

    public Set<String> getAllAliases() {
        return new HashSet<>(commandSection.names());
    }

    public void execute(CommandCause sender, String label, String[] arguments) {
        ParseableInput<?> input = ParseableInput.raw(arguments);
        Invocation<CommandCause> invocation = new Invocation<>(sender, new LiteSpongeSender(sender), this.commandSection.getName(), label, input);
        this.executeListener.execute(invocation, input);
    }

    public Collection<String> suggest(CommandCause sender, String label, String[] arguments) {
        SuggestionInput<?> input = SuggestionInput.raw(arguments);
        Invocation<CommandCause> invocation = new Invocation<>(sender, new LiteSpongeSender(sender), this.commandSection.getName(), label, input);
        return this.suggestionListener.suggest(invocation, input).asMultiLevelList();
    }

    public boolean canExecute(CommandCause sender) {
            MissingPermissions missingPermissions = MissingPermissions.check(new LiteSpongeSender(sender), this.commandSection);
            return missingPermissions.isPermitted();
    }
}
