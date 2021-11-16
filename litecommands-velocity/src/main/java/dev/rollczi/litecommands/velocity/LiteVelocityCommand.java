package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.LiteCommandManager;
import dev.rollczi.litecommands.component.ScopeMetaData;

import java.util.List;

public class LiteVelocityCommand implements SimpleCommand {

    private final ScopeMetaData scope;
    private final LiteCommandManager.Executor executor;
    private final LiteCommandManager.Suggester suggester;

    public LiteVelocityCommand(ScopeMetaData scope, LiteCommandManager.Executor executor, LiteCommandManager.Suggester suggester) {
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
    }

    @Override
    public void execute(Invocation invocation) {
        executor.execute(VelocityUtils.adaptInvocation(scope.getName(), invocation));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return suggester.suggest(VelocityUtils.adaptInvocation(scope.getName(), invocation));
    }

}
