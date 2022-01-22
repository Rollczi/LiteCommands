package dev.rollczi.litecommands;

import dev.rollczi.litecommands.platform.LitePlatformManager;
import dev.rollczi.litecommands.valid.messages.MessagesService;
import org.panda_lang.utilities.inject.Injector;

import java.util.logging.Logger;

public class LiteCommands {

    private final LiteRegisterResolvers registerResolvers;
    private final LitePlatformManager platformManager;
    private final MessagesService messagesService;
    private final Injector injector;
    private final Logger logger;

    LiteCommands(LiteRegisterResolvers registerResolvers, LitePlatformManager platformManager, MessagesService messagesService, Injector injector, Logger logger) {
        this.registerResolvers = registerResolvers;
        this.platformManager = platformManager;
        this.messagesService = messagesService;
        this.injector = injector;
        this.logger = logger;
    }

    public LiteRegisterResolvers getRegisterResolvers() {
        return registerResolvers;
    }

    public LitePlatformManager getPlatformManager() {
        return platformManager;
    }

    public MessagesService getMessagesService() {
        return messagesService;
    }

    public Injector getInjector() {
        return injector;
    }

    public Logger getLogger() {
        return logger;
    }

}
