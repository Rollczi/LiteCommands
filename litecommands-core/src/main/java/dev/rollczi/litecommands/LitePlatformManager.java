package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.ScopeMetaData;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.Collections;
import java.util.List;

public interface LitePlatformManager {

    void registerCommand(ScopeMetaData forScope, Executor execute, Suggester suggester);

    default void registerCommand(ScopeMetaData forScope, Executor execute) {
        registerCommand(forScope, execute, Suggester.NONE);
    }

    void unregisterCommands();

    interface Executor {

        void execute(LiteInvocation invocation) throws ValidationCommandException;

    }

    interface Suggester {

        List<String> suggest(LiteInvocation invocation);

        Suggester NONE = ignore -> Collections.emptyList();

    }

}
