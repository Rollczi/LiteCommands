package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.LiteSenderCreator;
import dev.rollczi.litecommands.platform.Suggester;
import dev.rollczi.litecommands.scope.ScopeMetaData;

import java.util.List;

public class LiteTestCommand {

    private final ScopeMetaData scope;
    private final Executor executor;
    private final Suggester suggester;
    private final LiteSenderCreator<Void> liteSenderCreator;

    public LiteTestCommand(ScopeMetaData scope, Executor executor, Suggester suggester, LiteSenderCreator<Void> creator) {
        this.scope = scope;
        this.executor = executor;
        this.suggester = suggester;
        this.liteSenderCreator = creator;
    }

    public ExecutionResult execute(LiteTestSender sender, String name, String alias, String[] arguments) {
        return executor.execute(new LiteInvocation(name, alias, sender, arguments));
    }

    public List<String> suggest(LiteTestSender sender, String name, String alias, String[] arguments) {
        return suggester.suggest(new LiteInvocation(name, alias, sender, arguments));
    }

    public ScopeMetaData getScope() {
        return scope;
    }

}
