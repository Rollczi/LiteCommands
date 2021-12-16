package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.LitePlatformManager;
import dev.rollczi.litecommands.component.ScopeMetaData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;

public class LiteBungeeCommand extends Command implements TabExecutor {

    private final ScopeMetaData scope;
    private final LitePlatformManager.Executor executor;
    private final LitePlatformManager.Suggester suggester;

    public LiteBungeeCommand(ScopeMetaData scope, LitePlatformManager.Executor executor, LitePlatformManager.Suggester suggester) {
        super(scope.getName(), "", scope.getAliases().toArray(new String[0]));
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        executor.execute(new LiteInvocation(scope.getName(), scope.getName(), new LiteBungeeSender(sender), args));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (LitePlatformManager.Suggester.NONE.equals(suggester)) {
            return Collections.emptyList();
        }

        return suggester.suggest(new LiteInvocation(scope.getName(), scope.getName(), new LiteBungeeSender(sender), args));
    }
}
