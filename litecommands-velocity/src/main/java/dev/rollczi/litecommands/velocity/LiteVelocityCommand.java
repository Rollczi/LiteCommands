package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.rollczi.litecommands.platform.LiteSenderCreator;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.Suggester;

import java.util.List;

public class LiteVelocityCommand implements SimpleCommand {

    private final LiteSenderCreator<CommandSource> liteSenderCreator;
    private final ScopeMetaData scope;
    private final Suggester suggester;
    private final Executor executor;

    public LiteVelocityCommand(ScopeMetaData scope, Executor executor, Suggester suggester, LiteSenderCreator<CommandSource> creator) {
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
        this.liteSenderCreator = creator;
    }

    @Override
    public void execute(Invocation invocation) {
        this.executor.execute(LiteVelocityUtils.adaptInvocation(this.scope.getName(), invocation, this.liteSenderCreator));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return this.suggester.suggest(LiteVelocityUtils.adaptSuggestInvocation(this.scope.getName(), invocation, this.liteSenderCreator));
    }

}
