package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.LiteSection;
import dev.rollczi.litecommands.valid.ValidationMessagesService;
import org.panda_lang.utilities.inject.Injector;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class LiteCommands {

    private final Set<LiteSection> resolvers;
    private final LiteCommandManager commandManager;
    private final ValidationMessagesService messagesService;
    private final Injector injector;
    private final Logger logger;

    LiteCommands(Set<LiteSection> resolvers, LiteCommandManager commandManager, ValidationMessagesService messagesService, Injector injector, Logger logger) {
        this.resolvers = resolvers;
        this.commandManager = commandManager;
        this.messagesService = messagesService;
        this.injector = injector;
        this.logger = logger;
    }

    public Collection<LiteSection> getResolvers() {
        return Collections.unmodifiableCollection(resolvers);
    }

    public LiteCommandManager getCommandManager() {
        return commandManager;
    }

    public ValidationMessagesService getMessagesService() {
        return messagesService;
    }

    public Injector getInjector() {
        return injector;
    }

    public Logger getLogger() {
        return logger;
    }

}
