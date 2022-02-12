package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.OptionArgumentHandler;
import dev.rollczi.litecommands.bind.NativeBind;
import dev.rollczi.litecommands.component.ExecutionResult;
import dev.rollczi.litecommands.argument.ArgumentHandler;
import dev.rollczi.litecommands.bind.LiteBind;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.LitePlatformManager;
import dev.rollczi.litecommands.platform.LiteSenderCreator;
import dev.rollczi.litecommands.platform.Suggester;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.messages.ContextualMessage;
import dev.rollczi.litecommands.valid.messages.LiteMessage;
import dev.rollczi.litecommands.valid.messages.MessagesService;
import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import dev.rollczi.litecommands.annotations.parser.LiteAnnotationParser;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.component.LiteComponentFactory;
import dev.rollczi.litecommands.bind.Bind;
import dev.rollczi.litecommands.utils.InjectUtils;
import dev.rollczi.litecommands.valid.handle.LiteExecutionResultHandler;
import dev.rollczi.litecommands.valid.handle.ExecutionResultHandler;
import dev.rollczi.litecommands.valid.messages.UseSchemeFormatting;
import org.panda_lang.utilities.inject.DependencyInjection;
import org.panda_lang.utilities.inject.Injector;
import panda.std.Option;
import panda.utilities.text.Formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class LiteCommandsBuilder<SENDER, P extends LitePlatformManager<SENDER>> {

    private final Set<Class<?>> commandClasses = new HashSet<>();
    private final Set<Object> commandInstances = new HashSet<>();
    private final Set<NativeBind> binds = new HashSet<>();
    private final Map<Class<?>, Set<ArgumentHandler<?>>> argumentHandlers = new HashMap<>();
    private final MessagesService messagesService = new MessagesService();
    private final LiteRegisterResolvers registerResolvers = new LiteRegisterResolvers();
    private final Formatter placeholders = new Formatter();
    private ExecutionResultHandler executionResultHandler;
    private P platformManager;
    private Injector baseInjector = DependencyInjection.createInjector();
    private Logger logger = Logger.getLogger("LiteCommands");

    public LiteCommandsBuilder() {
    }

    public LiteCommandsBuilder<SENDER, P> command(Collection<Class<?>> commands) {
        this.commandClasses.addAll(commands);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> command(Class<?>... commands) {
        return this.command(Arrays.asList(commands));
    }

    public LiteCommandsBuilder<SENDER, P> commandInstance(Collection<Object> commands) {
        this.commandInstances.addAll(commands);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> commandInstance(Object... commands) {
        return this.commandInstance(Arrays.asList(commands));
    }

    public LiteCommandsBuilder<SENDER, P> baseInjector(Injector baseInjector) {
        this.baseInjector = baseInjector;
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> bind(Collection<NativeBind> binds) {
        this.binds.addAll(binds);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> bind(NativeBind... bind) {
        return this.bind(Arrays.asList(bind));
    }

    public LiteCommandsBuilder<SENDER, P> bind(Bind... bind) {
        return this.bind(Arrays.asList(bind));
    }

    public <T> LiteCommandsBuilder<SENDER, P> bind(Class<T> on, LiteBind liteBind) {
        this.bind((resources) -> resources.on(on)
                .assignHandler((property, annotation, objects) -> liteBind.apply(InjectUtils.getContextFromInjectorArgs(objects).getInvocation())));
        return this;
    }

    public <T> LiteCommandsBuilder<SENDER, P> bind(Class<T> on, Object instance) {
        this.bind((resources) -> resources.on(on).assignInstance(instance));
        return this;
    }

    public <T> LiteCommandsBuilder<SENDER, P> bind(Class<T> on, Supplier<Object> supplier) {
        this.bind((resources) -> resources.on(on).assignInstance(supplier));
        return this;
    }

    public <T> LiteCommandsBuilder<SENDER, P> argument(Class<T> on, ArgumentHandler<T> argumentHandler) {
        Set<ArgumentHandler<?>> handlers = this.argumentHandlers.computeIfAbsent(on, key -> new HashSet<>());

        handlers.add(argumentHandler);
        return this;
    }

    public <T, O extends Option<?>> LiteCommandsBuilder<SENDER, P> argument(Class<O> optionalClass, OptionArgumentHandler<T> argumentHandler) {
        Set<ArgumentHandler<?>> handlers = this.argumentHandlers.computeIfAbsent(optionalClass, key -> new HashSet<>());

        handlers.add(argumentHandler);
        return this;
    }

    @Deprecated
    public LiteCommandsBuilder<SENDER, P> message(ValidationInfo validationInfo, ContextualMessage message) {
        this.messagesService.registerMessage(validationInfo, message);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> message(ValidationInfo validationInfo, LiteMessage message) {
        this.messagesService.registerMessage(validationInfo, message);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> formatting(UseSchemeFormatting formatting) {
        this.messagesService.setUseSchemeFormatting(formatting);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> placeholder(String key, Supplier<String> supplier) {
        this.placeholders.register(key, supplier);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> placeholder(String key, String text) {
        this.placeholders.register(key, text);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> placeholders(Map<String, Supplier<String>> placeholders) {
        placeholders.forEach(this.placeholders::register);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> executionResultHandler(ExecutionResultHandler executionResultHandler) {
        this.executionResultHandler = executionResultHandler;
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> platform(P platformManager) {
        this.platformManager = platformManager;
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> sender(LiteSenderCreator<SENDER> creator) {
        this.platformManager.setLiteSenderCreator(creator);
        return this;
    }

    public LiteCommandsBuilder<SENDER, P> logger(Logger logger) {
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

        AnnotationParser parser = new LiteAnnotationParser(argumentHandlers, placeholders);

        Injector injector = baseInjector.fork(resources -> {
            for (NativeBind bind : binds) {
                bind.bind(parser, resources);
            }
        });

        // Checks for legacy implementations
        for (Set<ArgumentHandler<?>> handlers : argumentHandlers.values()) {
            for (ArgumentHandler<?> handler : handlers) {
                if (handler.getClass().isAnnotationPresent(ArgumentName.class)) {
                    continue;
                }

                logger.warning( "annotation @ArgumentName isn't present before class " + handler.getClass());
            }
        }

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
            Executor executor = new DefaultExecutor(resolver, executionResultHandler);
            Suggester suggester = new DefaultSuggester(resolver);

            platformManager.registerCommand(resolver.getScope(), executor, suggester);
        }

        return new LiteCommands(registerResolvers, platformManager, messagesService, factory, parser, injector, logger);
    }

    public static <SENDER, P extends LitePlatformManager<SENDER>> LiteCommandsBuilder<SENDER, P> builder() {
        return new LiteCommandsBuilder<>();
    }

    private static class DefaultExecutor implements Executor {

        private final LiteComponent resolver;
        private final ExecutionResultHandler executionResultHandler;

        private DefaultExecutor(LiteComponent resolver, ExecutionResultHandler executionResultHandler) {
            this.resolver = resolver;
            this.executionResultHandler = executionResultHandler;
        }

        @Override
        public ExecutionResult execute(LiteInvocation invocation) throws ValidationCommandException {
            LiteComponent.ContextOfResolving context = LiteComponent.ContextOfResolving.create(invocation);
            ExecutionResult executionResult = resolver.resolveExecution(context);

            executionResultHandler.handle(executionResult);
            return executionResult;
        }

    }

    private static class DefaultSuggester implements Suggester {

        private final LiteComponent resolver;

        private DefaultSuggester(LiteComponent resolver) {
            this.resolver = resolver;
        }

        @Override
        public List<String> suggest(LiteInvocation invocation) {
            List<String> completions = resolver.resolveCompletion(LiteComponent.ContextOfResolving.create(invocation));
            String[] arguments = invocation.arguments();

            if (arguments.length == 0) {
                return completions;
            }

            String argument = arguments[arguments.length - 1];
            List<String> completionsStartsWith = new ArrayList<>();

            for (String completion : completions) {
                if (!completion.startsWith(argument)) {
                    continue;
                }

                completionsStartsWith.add(completion);
            }

            return completionsStartsWith;
        }

    }

}
