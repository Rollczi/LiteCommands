package dev.rollczi.litecommands;

import dev.rollczi.litecommands.valid.ValidationMessagesService;
import org.panda_lang.utilities.inject.Injector;

import java.util.logging.Logger;

public class LiteCommands {

    private final LiteRegisterResolvers registerResolvers;
    private final LitePlatformManager platformManager;
    private final ValidationMessagesService messagesService;
    private final Injector injector;
    private final Logger logger;

    LiteCommands(LiteRegisterResolvers registerResolvers, LitePlatformManager platformManager, ValidationMessagesService messagesService, Injector injector, Logger logger) {
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
