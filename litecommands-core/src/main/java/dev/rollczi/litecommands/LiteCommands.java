package dev.rollczi.litecommands;

import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.component.LiteComponentFactory;
import dev.rollczi.litecommands.platform.LitePlatformManager;
import dev.rollczi.litecommands.valid.messages.MessagesService;
import org.panda_lang.utilities.inject.Injector;

import java.util.logging.Logger;

public class LiteCommands {

    private final LiteRegisterResolvers registerResolvers;
    private final LitePlatformManager<?> platformManager;
    private final MessagesService messagesService;
    private final LiteComponentFactory componentFactory;
    private final AnnotationParser annotationParser;
    private final Injector injector;
    private final Logger logger;

    LiteCommands(LiteRegisterResolvers registerResolvers, LitePlatformManager<?> platformManager, MessagesService messagesService, LiteComponentFactory componentFactory, AnnotationParser annotationParser, Injector injector, Logger logger) {
        this.registerResolvers = registerResolvers;
        this.platformManager = platformManager;
        this.messagesService = messagesService;
        this.componentFactory = componentFactory;
        this.annotationParser = annotationParser;
        this.injector = injector;
        this.logger = logger;
    }

    public LiteRegisterResolvers getRegisterResolvers() {
        return registerResolvers;
    }

    public AnnotationParser getAnnotationParser() {
        return annotationParser;
    }

    public LiteComponentFactory getComponentFactory() {
        return componentFactory;
    }

    @SuppressWarnings("unchecked")
    public <SENDER, P extends LitePlatformManager<SENDER>> P getPlatformManager() {
        return (P) platformManager;
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
