package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.platform.LiteSenderCreator;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.Suggester;

import java.util.List;

public class LiteVelocityCommand implements SimpleCommand {

    private final ScopeMetaData scope;
    private final Executor executor;
    private final Suggester suggester;
    private final LiteSenderCreator<CommandSource> liteSenderCreator;

    public LiteVelocityCommand(ScopeMetaData scope, Executor executor, Suggester suggester, LiteSenderCreator<CommandSource> creator) {
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
        this.liteSenderCreator = creator;
    }

    @Override
    public void execute(Invocation invocation) {
        executor.execute(LiteVelocityUtils.adaptInvocation(scope.getName(), invocation, liteSenderCreator));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return suggester.suggest(LiteVelocityUtils.adaptInvocation(scope.getName(), invocation, liteSenderCreator));
    }

}
