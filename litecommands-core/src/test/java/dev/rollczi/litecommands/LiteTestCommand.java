package dev.rollczi.litecommands;

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

    public void execute(String name, String alias, String[] arguments) {
        executor.execute(new LiteInvocation(name, alias, liteSenderCreator.create(null), arguments));
    }

    public List<String> suggest(String name, String alias, String[] arguments) {
        return suggester.suggest(new LiteInvocation(name, alias, liteSenderCreator.create(null), arguments));
    }

    public ScopeMetaData getScope() {
        return scope;
    }

}
