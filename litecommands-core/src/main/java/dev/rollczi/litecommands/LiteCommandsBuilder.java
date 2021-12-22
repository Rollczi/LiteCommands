package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.inject.ArgumentHandler;
import dev.rollczi.litecommands.inject.ArgumentName;
import dev.rollczi.litecommands.inject.LiteBind;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.messages.ContextualMessage;
import dev.rollczi.litecommands.valid.messages.LiteMessage;
import dev.rollczi.litecommands.valid.messages.MessagesService;
import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.annotations.parser.LiteAnnotationParser;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.LiteComponentFactory;
import dev.rollczi.litecommands.inject.Bind;
import dev.rollczi.litecommands.inject.InjectContext;
import dev.rollczi.litecommands.inject.InjectUtils;
import dev.rollczi.litecommands.valid.handle.LiteExecutionResultHandler;
import dev.rollczi.litecommands.valid.handle.ExecutionResultHandler;
import dev.rollczi.litecommands.valid.messages.UseSchemeFormatting;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;
import panda.utilities.text.Formatter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class LiteCommandsBuilder {

    private final Set<Class<?>> commandClasses = new HashSet<>();
    private final Set<Object> commandInstances = new HashSet<>();
    private final Set<Bind> binds = new HashSet<>();
    private final Map<Class<?>, ArgumentHandler<?>> argumentHandlers = new HashMap<>();
    private final MessagesService messagesService = new MessagesService();
    private final LiteRegisterResolvers registerResolvers = new LiteRegisterResolvers();
    private final Formatter placeholders = new Formatter();
    private ExecutionResultHandler executionResultHandler;
    private LitePlatformManager platformManager;
    private Injector baseInjector = DependencyInjection.createInjector();
    private Logger logger = Logger.getLogger("LiteCommands");

    public LiteCommandsBuilder() {
    }

    public LiteCommandsBuilder command(Collection<Class<?>> commands) {
        this.commandClasses.addAll(commands);
        return this;
    }

    public LiteCommandsBuilder command(Class<?>... commands) {
        return this.command(Arrays.asList(commands));
    }

    public LiteCommandsBuilder commandInstance(Collection<Object> commands) {
        this.commandInstances.addAll(commands);
        return this;
    }

    public LiteCommandsBuilder commandInstance(Object... commands) {
        return this.commandInstance(Arrays.asList(commands));
    }

    public LiteCommandsBuilder bind(Collection<Bind> binds) {
        this.binds.addAll(binds);
        return this;
    }

    public LiteCommandsBuilder baseInjector(Injector baseInjector) {
        this.baseInjector = baseInjector;
        return this;
    }

    public LiteCommandsBuilder bind(Bind... bind) {
        return this.bind(Arrays.asList(bind));
    }

    public <T> LiteCommandsBuilder bind(Class<T> on, LiteBind liteBind) {
        this.bind((resources) -> resources.on(on)
                .assignHandler((property, annotation, objects) -> liteBind.apply(InjectUtils.getContextFromObjects(objects).getInvocation())));
        return this;
    }

    public <T> LiteCommandsBuilder bind(Class<T> on, Object instance) {
        this.bind((resources) -> resources.on(on).assignInstance(instance));
        return this;
    }

    public <T> LiteCommandsBuilder bind(Class<T> on, Supplier<Object> supplier) {
        this.bind((resources) -> resources.on(on).assignInstance(supplier));
        return this;
    }

    public <T> LiteCommandsBuilder argument(Class<T> on, ArgumentHandler<T> argumentHandler) {
        this.argumentHandlers.put(on, argumentHandler);
        return this;
    }

    @Deprecated
    public LiteCommandsBuilder message(ValidationInfo validationInfo, ContextualMessage message) {
        this.messagesService.registerMessage(validationInfo, message);
        return this;
    }

    public LiteCommandsBuilder message(ValidationInfo validationInfo, LiteMessage message) {
        this.messagesService.registerMessage(validationInfo, message);
        return this;
    }

    public LiteCommandsBuilder formatting(UseSchemeFormatting formatting) {
        this.messagesService.setUseSchemeFormatting(formatting);
        return this;
    }

    public LiteCommandsBuilder placeholder(String key, Supplier<String> supplier) {
        this.placeholders.register(key, supplier);
        return this;
    }

    public LiteCommandsBuilder placeholder(String key, String text) {
        this.placeholders.register(key, text);
        return this;
    }

    public LiteCommandsBuilder placeholders(Map<String, Supplier<String>> placeholders) {
        placeholders.forEach(this.placeholders::register);
        return this;
    }

    public LiteCommandsBuilder platform(LitePlatformManager platformManager) {
        this.platformManager = platformManager;
        return this;
    }

    public LiteCommandsBuilder logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public LiteCommands register() {
        if (platformManager == null) {
            throw new NullPointerException();
        }

        if (executionResultHandler == null) {
            executionResultHandler = new LiteExecutionResultHandler(messagesService);
        }

        Injector injector = baseInjector.fork(resources -> {
            for (Bind bind : binds) {
                bind.bind(resources);
            }

            resources.annotatedWith(Arg.class).assignHandler((property, arg, objects) -> {
                InjectContext context = InjectUtils.getContextFromObjects(objects);

                for (Map.Entry<Class<?>, ArgumentHandler<?>> entry : argumentHandlers.entrySet()) {
                    Class<?> on = entry.getKey();
                    ArgumentHandler<?> argumentHandler = entry.getValue();

                    if (!on.isAssignableFrom(property.getType())) {
                        continue;
                    }

                    return argumentHandler.parse(context, arg.value());
                }

                return null;
            });
        });

        // Checks for legacy implementations
        for (ArgumentHandler<?> handler : argumentHandlers.values()) {
            if (handler.getClass().isAnnotationPresent(ArgumentName.class)) {
                continue;
            }

            logger.warning( "annotation @ArgumentName isn't present before class " + handler.getClass());
        }

        AnnotationParser parser = new LiteAnnotationParser(argumentHandlers, placeholders);
        LiteComponentFactory factory = new LiteComponentFactory(logger, injector, parser);

        for (Object instance : commandInstances) {
            registerResolvers.register(factory.createSection(instance)
                    .orElseThrow(RuntimeException::new));
        }

        for (Class<?> commandClass : commandClasses) {
            registerResolvers.register(factory.createSection(commandClass)
                    .orElseThrow(RuntimeException::new));
        }

        for (LiteComponent resolver : registerResolvers.getResolvers().values()) {
            platformManager.registerCommand(resolver.getScope(), invocation -> {
                LiteComponent.ContextOfResolving context = LiteComponent.ContextOfResolving.create(invocation);
                ExecutionResult executionResult = resolver.resolveExecution(context);

                executionResultHandler.handle(executionResult);
            }, invocation -> resolver.resolveCompletion(LiteComponent.ContextOfResolving.create(invocation)));
        }

        return new LiteCommands(registerResolvers, platformManager, messagesService, injector, logger);
    }

    public static LiteCommandsBuilder builder() {
        return new LiteCommandsBuilder();
    }

}
