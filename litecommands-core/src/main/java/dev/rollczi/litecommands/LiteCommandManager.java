package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.ScopeMetaData;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.List;

public interface LiteCommandManager {

    void registerCommand(ScopeMetaData forScope, Executor execute, Suggester suggester);

    void unregisterCommands();

    interface Executor {

        void execute(LiteInvocation invocation) throws ValidationCommandException;

    }

    interface Suggester {

        List<String> suggest(LiteInvocation invocation);

    }

}
