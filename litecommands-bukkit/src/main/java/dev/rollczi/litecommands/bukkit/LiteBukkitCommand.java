package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandManager;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.ScopeMetaData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class LiteBukkitCommand extends Command {

    private final ScopeMetaData scope;
    private final LiteCommandManager.Executor executor;
    private final LiteCommandManager.Suggester suggester;

    public LiteBukkitCommand(ScopeMetaData scope, LiteCommandManager.Executor executor, LiteCommandManager.Suggester suggester) {
        super(scope.getName(), "", "/" + scope.getName(), new ArrayList<>(scope.getAliases()));
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        executor.execute(new LiteInvocation(scope.getName(), alias, new LiteBukkitSender(sender), args));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return suggester.suggest(new LiteInvocation(scope.getName(), alias, new LiteBukkitSender(sender), args));
    }

}
