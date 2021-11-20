package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LitePlatformManager;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.ScopeMetaData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class LiteBukkitCommand extends Command {

    private final ScopeMetaData scope;
    private final LitePlatformManager.Executor executor;
    private final LitePlatformManager.Suggester suggester;

    public LiteBukkitCommand(ScopeMetaData scope, LitePlatformManager.Executor executor, LitePlatformManager.Suggester suggester) {
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
        if (LitePlatformManager.Suggester.NONE.equals(suggester)) {
            return super.tabComplete(sender, alias, args);
        }

        return suggester.suggest(new LiteInvocation(scope.getName(), alias, new LiteBukkitSender(sender), args));
    }

}
