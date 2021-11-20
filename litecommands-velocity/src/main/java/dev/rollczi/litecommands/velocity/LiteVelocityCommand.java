package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.LitePlatformManager;
import dev.rollczi.litecommands.component.ScopeMetaData;

import java.util.List;

public class LiteVelocityCommand implements SimpleCommand {

    private final ScopeMetaData scope;
    private final LitePlatformManager.Executor executor;
    private final LitePlatformManager.Suggester suggester;

    public LiteVelocityCommand(ScopeMetaData scope, LitePlatformManager.Executor executor, LitePlatformManager.Suggester suggester) {
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
    }

    @Override
    public void execute(Invocation invocation) {
        executor.execute(LiteVelocityUtils.adaptInvocation(scope.getName(), invocation));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return suggester.suggest(LiteVelocityUtils.adaptInvocation(scope.getName(), invocation));
    }

}
