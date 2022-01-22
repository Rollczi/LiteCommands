package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.platform.LiteSenderCreator;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.Suggester;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;

public class LiteBungeeCommand extends Command implements TabExecutor {

    private final ScopeMetaData scope;
    private final Executor executor;
    private final Suggester suggester;
    private final LiteSenderCreator<CommandSender> liteSenderCreator;

    public LiteBungeeCommand(ScopeMetaData scope, Executor executor, Suggester suggester, LiteSenderCreator<CommandSender> creator) {
        super(scope.getName(), "", scope.getAliases().toArray(new String[0]));
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
        this.liteSenderCreator = creator;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        executor.execute(new LiteInvocation(scope.getName(), scope.getName(), liteSenderCreator.create(sender), args));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (Suggester.NONE.equals(suggester)) {
            return Collections.emptyList();
        }

        return suggester.suggest(new LiteInvocation(scope.getName(), scope.getName(), liteSenderCreator.create(sender), args));
    }

}
