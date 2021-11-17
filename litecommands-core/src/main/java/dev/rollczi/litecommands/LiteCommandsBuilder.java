package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.LiteSection;
import dev.rollczi.litecommands.inject.LiteBind;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.ValidationMessagesService;
import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.annotations.parser.LiteAnnotationParser;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.LiteComponentFactory;
import dev.rollczi.litecommands.inject.Bind;
import dev.rollczi.litecommands.inject.InjectContext;
import dev.rollczi.litecommands.inject.InjectUtils;
import dev.rollczi.litecommands.inject.SingleArgumentHandler;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.handle.LiteValidationExceptionHandler;
import dev.rollczi.litecommands.valid.handle.ValidationExceptionHandler;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class LiteCommandsBuilder {

    private final Set<Class<?>> commandClasses = new HashSet<>();
    private final Set<Object> commandInstances = new HashSet<>();
    private final Set<Bind> binds = new HashSet<>();
    private final Map<Class<?>, SingleArgumentHandler<?>> argumentHandlers = new HashMap<>();
    private final ValidationMessagesService messagesService = new ValidationMessagesService();
    private ValidationExceptionHandler validationExceptionHandler;
    private LiteCommandManager commandManager;
    private Logger logger;

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

    public <T> LiteCommandsBuilder argument(Class<T> on, SingleArgumentHandler<T> singleArgumentHandler) {
        this.argumentHandlers.put(on, singleArgumentHandler);
        return this;
    }

    public LiteCommandsBuilder message(ValidationInfo validationInfo, String message) {
        this.messagesService.registerMessage(validationInfo, message);
        return this;
    }

    public LiteCommandsBuilder registrationResolver(LiteCommandManager registrationResolver) {
        this.commandManager = registrationResolver;
        return this;
    }

    public LiteCommandsBuilder logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public LiteCommands register() {
        if (commandManager == null) {
            throw new NullPointerException();
        }

        if (logger == null) {
            logger = Logger.getLogger("LiteCommands");
        }

        if (validationExceptionHandler == null) {
            validationExceptionHandler = new LiteValidationExceptionHandler(messagesService);
        }

        Injector injector = DependencyInjection.createInjector(resources -> {
            binds.forEach(bind -> bind.bind(resources));
            resources.annotatedWith(Arg.class).assignHandler((property, arg, objects) -> {
                InjectContext context = InjectUtils.getContextFromObjects(objects);
                LiteInvocation invocation = context.getInvocation();

                for (Map.Entry<Class<?>, SingleArgumentHandler<?>> entry : argumentHandlers.entrySet()) {
                    Class<?> on = entry.getKey();
                    SingleArgumentHandler<?> singleArgumentHandler = entry.getValue();

                    if (!on.isAssignableFrom(property.getType())) {
                        continue;
                    }

                    return singleArgumentHandler.parse(invocation.arguments()[context.getArgsMargin() + arg.value()]);
                }

                return null;
            });
        });

        AnnotationParser parser = new LiteAnnotationParser(argumentHandlers);
        LiteComponentFactory factory = new LiteComponentFactory(logger, injector, parser);
        Set<LiteSection> resolvers = new HashSet<>();

        for (Object instance : commandInstances) {
            resolvers.add(factory.createSection(instance)
                    .orThrow(() -> new IllegalArgumentException(instance.getClass() + " instance isn't a section")));
        }

        for (Class<?> commandClass : commandClasses) {
            try {
                resolvers.add(factory.createSection(commandClass)
                        .orThrow(() -> new IllegalArgumentException(commandClass + " class isn't a section")));
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        for (LiteSection resolver : resolvers) {
            commandManager.registerCommand(resolver.getScope(), invocation -> {
                try {
                    resolver.resolveExecution(LiteComponent.MetaData.create(invocation));
                } catch (ValidationCommandException exception) {
                    validationExceptionHandler.handle(invocation, exception);
                }
            }, invocation -> resolver.resolveCompletion(LiteComponent.MetaData.create(invocation)));
        }

        return new LiteCommands(resolvers, commandManager, messagesService, injector, logger);
    }

    public static LiteCommandsBuilder builder() {
        return new LiteCommandsBuilder();
    }

}
