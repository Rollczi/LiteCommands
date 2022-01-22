package dev.rollczi.litecommands.platform;

import dev.rollczi.litecommands.scope.ScopeMetaData;

public interface LitePlatformManager<T> {

    void registerCommand(ScopeMetaData forScope, Executor execute, Suggester suggester);

    default void registerCommand(ScopeMetaData forScope, Executor execute) {
        registerCommand(forScope, execute, Suggester.NONE);
    }

    void unregisterCommands();

    LiteSenderCreator<T> getLiteSenderCreator();

    void setLiteSenderCreator(LiteSenderCreator<T> liteSenderCreator);

}
