package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Objects;

class BungeeCommand extends Command implements TabExecutor {

    private final LiteBungeeSettings settings;
    private final CommandRoute<CommandSender> commandSection;
    private final String label;
    private final PlatformInvocationListener<CommandSender> executeListener;
    private final PlatformSuggestionListener<CommandSender> suggestionListener;

    public BungeeCommand(LiteBungeeSettings settings, CommandRoute<CommandSender> command, String label, PlatformInvocationListener<CommandSender> executeListener, PlatformSuggestionListener<CommandSender> suggestionListener) {
        super(label, "", label);
        this.settings = settings;
        this.commandSection = command;
        this.label = label;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ParseableInput<?> input = ParseableInput.raw(args);
        BungeeSender platformSender = new BungeeSender(sender);
        Invocation<CommandSender> invocation = new Invocation<>(sender, platformSender, this.commandSection.getName(), this.label, input);

        this.executeListener.execute(invocation, input);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        SuggestionInput<?> input = SuggestionInput.raw(args);
        BungeeSender platformSender = new BungeeSender(sender);
        Invocation<CommandSender> invocation = new Invocation<>(sender, platformSender, this.commandSection.getName(), this.label, input);

        return this.suggestionListener.suggest(invocation, input)
            .asMultiLevelList();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        boolean isNative = commandSection.meta().get(Meta.NATIVE_PERMISSIONS);

        if (isNative || settings.isNativePermissions()) {
            MissingPermissions missingPermissions = MissingPermissions.check(new BungeeSender(sender), this.commandSection);

            return missingPermissions.isPermitted();
        }

        return super.hasPermission(sender);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BungeeCommand)) return false;
        if (!super.equals(o)) return false;
        BungeeCommand that = (BungeeCommand) o;
        return Objects.equals(commandSection, that.commandSection) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commandSection, label);
    }

}
