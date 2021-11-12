package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.ScopeMetaData;
import dev.rollczi.litecommands.valid.ValidationCommandException;

public interface LiteCommandManager {

    void registerCommand(ScopeMetaData forScope, CommandInvocationExecutor execute);

    void unregisterCommands();

    @FunctionalInterface
    interface CommandInvocationExecutor {

        void execute(LiteInvocation invocation) throws ValidationCommandException;

    }

}
