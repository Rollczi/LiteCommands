package dev.rollczi.litecommands;

import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserChained;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.parser.ParserRegistryImpl;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBaseChained;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterChained;
import dev.rollczi.litecommands.bind.BindChainedProvider;
import dev.rollczi.litecommands.command.CommandMerger;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.context.ContextChainedProvider;
import dev.rollczi.litecommands.extension.LiteCommandsProviderExtension;
import dev.rollczi.litecommands.extension.annotations.AnnotationsExtension;
import dev.rollczi.litecommands.extension.annotations.LiteAnnotationsProcessorExtension;
import dev.rollczi.litecommands.processor.LiteBuilderProcessor;
import dev.rollczi.litecommands.command.executor.CommandExecuteService;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.extension.LiteExtension;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.editor.Editor;
import dev.rollczi.litecommands.handler.exception.ExceptionHandler;
import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.handler.result.ResultHandleServiceImpl;
import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.message.InvokedMessage;
import dev.rollczi.litecommands.message.Message;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import dev.rollczi.litecommands.platform.PlatformSettingsConfigurator;
import dev.rollczi.litecommands.programmatic.LiteCommand;
import dev.rollczi.litecommands.programmatic.LiteCommandsProgrammatic;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerSameThreadImpl;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.schematic.SimpleSchematicGenerator;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.SuggestionService;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistryImpl;
import dev.rollczi.litecommands.shared.Preconditions;
import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderCollector;
import dev.rollczi.litecommands.editor.EditorService;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class LiteCommandsBaseBuilder<SENDER, C extends PlatformSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> implements
    LiteCommandsBuilder<SENDER, C, B>,
    LiteCommandsInternal<SENDER, C> {

    protected final Class<SENDER> senderClass;
    protected final Platform<SENDER, C> platform;

    protected final Set<LiteBuilderProcessor<SENDER, C>> preProcessors = new LinkedHashSet<>();
    protected final Set<LiteBuilderProcessor<SENDER, C>> postProcessors = new LinkedHashSet<>();

    protected final List<LiteExtension<SENDER, ?>> extensions = new ArrayList<>();
    protected final List<LiteCommandsProviderExtension<SENDER, ?>> commandsProviderExtensions = new ArrayList<>();

    protected final EditorService<SENDER> editorService;
    protected final ValidatorService<SENDER> validatorService;
    protected final ParserRegistry<SENDER> parserRegistry;
    protected final SuggesterRegistry<SENDER> suggesterRegistry;
    protected final BindRegistry bindRegistry;
    protected final ContextRegistry<SENDER> contextRegistry;
    protected final ResultHandleService<SENDER> resultHandleService;
    protected final CommandBuilderCollector<SENDER> commandBuilderCollector;
    protected final MessageRegistry<SENDER> messageRegistry;
    protected final WrapperRegistry wrapperRegistry;

    protected Scheduler scheduler;
    protected SchematicGenerator<SENDER> schematicGenerator;

    /**
     * Constructor for {@link LiteCommandsBaseBuilder}
     *
     * @param senderClass class of sender
     * @param platform platform
     */
    public LiteCommandsBaseBuilder(Class<SENDER> senderClass, Platform<SENDER, C> platform) {
        this(
            senderClass,
            platform,
            new EditorService<>(),
            new ValidatorService<>(),
            new ParserRegistryImpl<>(),
            new SuggesterRegistryImpl<>(),
            new BindRegistry(),
            new ContextRegistry<>(),
            new ResultHandleServiceImpl<>(),
            new CommandBuilderCollector<>(),
            new MessageRegistry<>(),
            new WrapperRegistry()
        );
    }

    /**
     * Copy constructor
     */
    @ApiStatus.Experimental
    public LiteCommandsBaseBuilder(
        Class<SENDER> senderClass,
        Platform<SENDER, C> platform,

        EditorService<SENDER> editorService,
        ValidatorService<SENDER> validatorService,
        ParserRegistry<SENDER> parserRegistry,
        SuggesterRegistry<SENDER> suggesterRegistry,
        BindRegistry bindRegistry,
        ContextRegistry<SENDER> contextRegistry,
        ResultHandleService<SENDER> resultHandleService,
        CommandBuilderCollector<SENDER> commandBuilderCollector,
        MessageRegistry<SENDER> messageRegistry,
        WrapperRegistry wrapperRegistry
    ) {
        this.senderClass = senderClass;
        this.platform = platform;

        this.editorService = editorService;
        this.validatorService = validatorService;
        this.parserRegistry = parserRegistry;
        this.suggesterRegistry = suggesterRegistry;
        this.bindRegistry = bindRegistry;
        this.contextRegistry = contextRegistry;
        this.resultHandleService = resultHandleService;
        this.commandBuilderCollector = commandBuilderCollector;
        this.messageRegistry = messageRegistry;
        this.wrapperRegistry = wrapperRegistry;

        this.scheduler = new SchedulerSameThreadImpl();
        this.schematicGenerator = new SimpleSchematicGenerator<>(SchematicFormat.angleBrackets(), validatorService, wrapperRegistry);
    }

    @Override
    public B settings(PlatformSettingsConfigurator<C> configurator) {
        C newConfig = configurator.apply(this.platform.getConfiguration());
        Preconditions.notNull(newConfig, "configuration");

        this.platform.setConfiguration(newConfig);

        return this.self();
    }

    @Override
    public B commands(LiteCommandsProvider<SENDER> commandsProvider) {
        this.preProcessExtensionsOnProvider(commandsProvider);
        this.commandBuilderCollector.add(commandsProvider.toInternalProvider(this));
        return this.self();
    }

    @Override
    @SuppressWarnings("unchecked")
    public B commands(Object... commands) {
        List<LiteCommandsProvider<SENDER>> providers = new ArrayList<>();
        Collection<LiteCommand<SENDER>> programmatic = new ArrayList<>();
        List<Class<?>> classes = new ArrayList<>();
        List<Object> instances = new ArrayList<>();

        for (Object command : commands) {
            if (command instanceof LiteCommandsProvider) {
                providers.add((LiteCommandsProvider<SENDER>) command);
                continue;
            }

            if (command instanceof LiteCommand) {
                programmatic.add((LiteCommand<SENDER>) command);
                continue;
            }

            if (command instanceof Class) {
                classes.add((Class<?>) command);
                continue;
            }

            instances.add(command);
        }

        for (LiteCommandsProvider<SENDER> provider : providers) {
            this.commands(provider);
        }

        if (!programmatic.isEmpty()) {
            this.commands(LiteCommandsProgrammatic.of(programmatic));
        }

        if (!classes.isEmpty() || !instances.isEmpty()) {
            this.commands(LiteCommandsAnnotations.<SENDER>create()
                .load(instances.toArray(new Object[0]))
                .loadClasses(classes.toArray(new Class<?>[0]))
            );
        }

        return this.self();
    }

    /**
     * Pre-process extensions on provider before executing {@link CommandBuilderCollector#collectCommands()}
     * Kinda magic, but it works.
     *
     * @param commandsProvider provider of commands.
     */
    private void preProcessExtensionsOnProvider(LiteCommandsProvider<SENDER> commandsProvider) {
        this.preProcessor((builder, internal) -> {
            for (LiteCommandsProviderExtension<SENDER, ?> extension : commandsProviderExtensions) {
                extension.extendCommandsProvider(this, this, commandsProvider);
            }
        });
    }

    @Override
    public <T> B argumentParser(Class<T> type, Parser<SENDER, T> parser) {
        return argumentParser(TypeRange.same(type), ArgumentKey.of(), parser);
    }

    @Override
    public <T> B argumentParser(Class<T> type, ParserChained<SENDER, T> parser) {
        return argumentParser(TypeRange.same(type), ArgumentKey.of(), parser);
    }

    @Override
    public <PARSED>
    B argumentParser(Class<PARSED> type, ArgumentKey key, Parser<SENDER, PARSED> parser) {
        return argumentParser(TypeRange.same(type), key, parser);
    }

    @Override
    public <PARSED>
    B argumentParser(Class<PARSED> type, ArgumentKey key, ParserChained<SENDER, PARSED> parser) {
        return argumentParser(TypeRange.same(type), key, parser);
    }

    @Override
    public <PARSED>
    B argumentParser(TypeRange<PARSED> type, ArgumentKey key, Parser<SENDER, PARSED> parser) {
        this.parserRegistry.registerParser(type, key, parser);
        return this.self();
    }

    @Override
    public <PARSED>
    B argumentParser(TypeRange<PARSED> type, ArgumentKey key, ParserChained<SENDER, PARSED> parser) {
        this.parserRegistry.registerParser(type, key, parser);
        return this.self();
    }

    @Override
    public <T> B argumentSuggestion(Class<T> type, SuggestionResult suggestion) {
        return argumentSuggestion(TypeRange.same(type), ArgumentKey.of(), suggestion);
    }

    @Override
    public <T> B argumentSuggestion(Class<T> type, ArgumentKey key, SuggestionResult suggestion) {
        return argumentSuggestion(TypeRange.same(type), key, suggestion);
    }

    @Override
    public <T> B argumentSuggestion(TypeRange<T> type, ArgumentKey key, SuggestionResult suggestion) {
        this.suggesterRegistry.registerSuggester(type, key, (invocation, argument, context) -> suggestion);
        return this.self();
    }

    @Override
    public <T>
    B argumentSuggester(Class<T> type, Suggester<SENDER, T> suggester) {
        return argumentSuggester(TypeRange.same(type), ArgumentKey.of(), suggester);
    }

    @Override
    public <T>
    B argumentSuggester(Class<T> type, SuggesterChained<SENDER, T> suggester) {
        return argumentSuggester(TypeRange.same(type), ArgumentKey.of(), suggester);
    }

    @Override
    public <T>
    B argumentSuggester(Class<T> type, ArgumentKey key, Suggester<SENDER, T> suggester) {
        return argumentSuggester(TypeRange.same(type), key, suggester);
    }

    @Override
    public <T>
    B argumentSuggester(Class<T> type, ArgumentKey key, SuggesterChained<SENDER, T> suggester) {
        return argumentSuggester(TypeRange.same(type), key, suggester);
    }

    @Override
    public <T>
    B argumentSuggester(TypeRange<T> type, ArgumentKey key, Suggester<SENDER, T> suggester) {
        this.suggesterRegistry.registerSuggester(type, key, suggester);
        return this.self();
    }

    @Override
    public <T>
    B argumentSuggester(TypeRange<T> type, ArgumentKey key, SuggesterChained<SENDER, T> suggester) {
        this.suggesterRegistry.registerSuggester(type, key, suggester);
        return this.self();
    }

    @Override
    public <T> B argument(Class<T> type, ArgumentResolverBase<SENDER, T> resolver) {
        return argument(TypeRange.same(type), ArgumentKey.of(), resolver);
    }

    @Override
    public <T> B argument(Class<T> type, ArgumentResolverBaseChained<SENDER, T> resolver) {
        return argument(TypeRange.same(type), ArgumentKey.of(), resolver);
    }

    @Override
    public <T> B argument(Class<T> type, ArgumentKey key, ArgumentResolverBase<SENDER, T> resolver) {
        return argument(TypeRange.same(type), key, resolver);
    }

    @Override
    public <T> B argument(Class<T> type, ArgumentKey key, ArgumentResolverBaseChained<SENDER, T> resolver) {
        return argument(TypeRange.same(type), key, resolver);
    }

    @Override
    public <T> B argument(TypeRange<T> type, ArgumentResolverBase<SENDER, T> resolver) {
        return argument(type, ArgumentKey.of(), resolver);
    }

    @Override
    public <T> B argument(TypeRange<T> type, ArgumentResolverBaseChained<SENDER, T> resolver) {
        return argument(type, ArgumentKey.of(), resolver);
    }

    @Override
    public <T> B argument(TypeRange<T> type, ArgumentKey key, ArgumentResolverBase<SENDER, T> resolver) {
        this.argumentParser(type, key, resolver);
        this.argumentSuggester(type, key, resolver);
        return this.self();
    }

    @Override
    public <T> B argument(TypeRange<T> type, ArgumentKey key, ArgumentResolverBaseChained<SENDER, T> resolver) {
        this.argumentParser(type, key, resolver);
        this.argumentSuggester(type, key, resolver);
        return this.self();
    }

    @Override
    public <T> B context(Class<T> on, ContextChainedProvider<SENDER, T> bind) {
        this.contextRegistry.registerProvider(on, bind);
        return this.self();
    }

    @Override
    public <T> B bind(Class<T> on, BindChainedProvider<T> bindProvider) {
        this.bindRegistry.bindInstance(on, bindProvider);
        return this.self();
    }

    @Override
    public <T> B bind(Class<T> on, Supplier<T> bind) {
        this.bindRegistry.bindInstance(on, bind);
        return this.self();
    }

    @Override
    public B bindUnsafe(Class<?> on, Supplier<?> bind) {
        this.bindRegistry.bindInstanceUnsafe(on, bind);
        return this.self();
    }

    @Override
    public B scheduler(Scheduler scheduler) {
        this.scheduler.shutdown();
        this.scheduler = scheduler;
        return this.self();
    }

    @Override
    public <T, CONTEXT> B message(MessageKey<CONTEXT> key, Message<T, CONTEXT> message) {
        this.messageRegistry.register(key, message);
        return this.self();
    }

    @Override
    public <T, CONTEXT> B message(MessageKey<CONTEXT> key, InvokedMessage<SENDER, T, CONTEXT> message) {
        this.messageRegistry.register(key, message);
        return this.self();
    }

    @Override
    public <T, CONTEXT> B message(MessageKey<CONTEXT> key, T message) {
        this.messageRegistry.register(key, context -> message);
        return this.self();
    }

    @Override
    public B editorGlobal(Editor<SENDER> editor) {
        this.editorService.editorGlobal(editor);
        return this.self();
    }

    @Override
    public B editor(Scope scope, Editor<SENDER> editor) {
        this.editorService.editor(scope, editor);
        return this.self();
    }

    @Override
    public B validatorGlobal(Validator<SENDER> validator) {
        this.validatorService.registerValidatorGlobal(validator);
        return this.self();
    }

    @Override
    public B validator(Scope scope, Validator<SENDER> validator) {
        this.validatorService.registerValidator(scope, validator);
        return this.self();
    }

    @Override
    public <T> B result(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler) {
        this.resultHandleService.registerHandler(resultType, handler);
        return this.self();
    }

    @Override
    public B resultUnexpected(ResultHandler<SENDER, Object> handler) {
        this.resultHandleService.registerHandler(Object.class, handler);
        return this.self();
    }

    @Override
    public <E extends Throwable> B exception(Class<E> exceptionType, ExceptionHandler<SENDER, ? extends E> handler) {
        this.resultHandleService.registerHandler(exceptionType, handler);
        return this.self();
    }

    @Override
    public B exceptionUnexpected(ExceptionHandler<SENDER, Throwable> handler) {
        this.resultHandleService.registerHandler(Throwable.class, handler);
        return this.self();
    }

    @Override
    public B missingPermission(MissingPermissionsHandler<SENDER> handler) {
        this.resultHandleService.registerHandler(MissingPermissions.class, handler);
        return this.self();
    }

    @Override
    public B invalidUsage(InvalidUsageHandler<SENDER> handler) {
        this.resultHandleService.registerHandler(InvalidUsage.class, handler);
        return this.self();
    }

    @Override
    public B schematicGenerator(SchematicGenerator<SENDER> schematicGenerator) {
        this.schematicGenerator = schematicGenerator;
        return this.self();
    }

    @Override
    public B schematicGenerator(SchematicFormat format) {
        this.schematicGenerator = new SimpleSchematicGenerator<>(format, validatorService, wrapperRegistry);
        return this.self();
    }

    @Override
    public B wrapper(Wrapper wrapper) {
        this.wrapperRegistry.registerFactory(wrapper);
        return this.self();
    }

    @Override
    public B selfProcessor(LiteBuilderProcessor<SENDER, C> processor) {
        processor.process(this, this);
        return this.self();
    }

    @Override
    public B preProcessor(LiteBuilderProcessor<SENDER, C> preProcessor) {
        this.preProcessors.add(preProcessor);
        return this.self();
    }

    @Override
    public B postProcessor(LiteBuilderProcessor<SENDER, C> postProcessor) {
        this.postProcessors.add(postProcessor);
        return this.self();
    }

    @Override
    public <CONFIGURATION> B extension(LiteExtension<SENDER, CONFIGURATION> extension) {
        return this.extension(extension, configuration -> {});
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CONFIGURATION, E extends LiteExtension<SENDER, CONFIGURATION>> B extension(E extension, LiteConfigurator<CONFIGURATION> configurator) {
        extension.configure(configurator);
        extension.extend(this, this);
        extensions.add(extension);

        if (extension instanceof LiteCommandsProviderExtension) {
            commandsProviderExtensions.add((LiteCommandsProviderExtension<SENDER, CONFIGURATION>) extension);
        }

        return this.self();
    }

    @Override
    public B annotations(LiteConfigurator<AnnotationsExtension<SENDER>> configuration) {
        return this.extension(new LiteAnnotationsProcessorExtension<>(), configuration);
    }

    @Override
    public LiteCommands<SENDER> build() {
        return this.build(true);
    }

    @Override
    public LiteCommands<SENDER> build(boolean register) {
        if (this.platform == null) {
            throw new IllegalStateException("No platform was set");
        }

        for (LiteBuilderProcessor<SENDER, C> processor : preProcessors) {
            processor.process(this, this);
        }

        CommandExecuteService<SENDER> commandExecuteService = new CommandExecuteService<>(validatorService, resultHandleService, scheduler, schematicGenerator, parserRegistry, contextRegistry, wrapperRegistry, bindRegistry);
        SuggestionService<SENDER> suggestionService = new SuggestionService<>(parserRegistry, suggesterRegistry, validatorService);
        CommandManager<SENDER> commandManager = new CommandManager<>(this.platform, commandExecuteService, suggestionService);

        CommandMerger<SENDER> commandMerger = new CommandMerger<>();

        for (CommandBuilder<SENDER> collected : this.commandBuilderCollector.collectCommands()) {
            CommandBuilder<SENDER> edited = editorService.edit(collected);

            if (!edited.buildable()) {
                continue;
            }

            for (CommandRoute<SENDER> commandRoute : edited.build(commandManager.getRoot())) {
                commandMerger.merge(commandRoute);
            }
        }

        for (CommandRoute<SENDER> mergedCommand : commandMerger.getMergedCommands()) {
            commandManager.register(mergedCommand);
        }

        for (LiteBuilderProcessor<SENDER, C> processor : postProcessors) {
            processor.process(this, this);
        }

        LiteCommands<SENDER> liteCommand = new LiteCommandsImpl<>(commandManager, this);

        if (register) {
            liteCommand.register();
        }

        return liteCommand;
    }

    /**
     * Internal API
     */

    @Override
    @ApiStatus.Internal
    public Class<SENDER> getSenderClass() {
        return this.senderClass;
    }

    @Override
    @ApiStatus.Internal
    public Platform<SENDER, C> getPlatform() {
        return this.platform;
    }

    @Override
    @ApiStatus.Internal
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    @ApiStatus.Internal
    public SchematicGenerator<SENDER> getSchematicGenerator() {
        return this.schematicGenerator;
    }

    @Override
    @ApiStatus.Internal
    public EditorService<SENDER> getEditorService() {
        return this.editorService;
    }

    @Override
    @ApiStatus.Internal
    public ValidatorService<SENDER> getValidatorService() {
        return this.validatorService;
    }

    @Override
    @ApiStatus.Internal
    public ParserRegistry<SENDER> getParserRegistry() {
        return this.parserRegistry;
    }

    @Override
    @ApiStatus.Internal
    public SuggesterRegistry<SENDER> getSuggesterRegistry() {
        return this.suggesterRegistry;
    }

    @Override
    @ApiStatus.Internal
    public BindRegistry getBindRegistry() {
        return this.bindRegistry;
    }

    @Override
    @ApiStatus.Internal
    public ContextRegistry<SENDER> getContextRegistry() {
        return this.contextRegistry;
    }

    @Override
    @ApiStatus.Internal
    public ResultHandleService<SENDER> getResultService() {
        return this.resultHandleService;
    }

    @Override
    @ApiStatus.Internal
    public CommandBuilderCollector<SENDER> getCommandBuilderCollector() {
        return this.commandBuilderCollector;
    }

    @Override
    @ApiStatus.Internal
    public MessageRegistry<SENDER> getMessageRegistry() {
        return this.messageRegistry;
    }

    @Override
    @ApiStatus.Internal
    public WrapperRegistry getWrapperRegistry() {
        return this.wrapperRegistry;
    }

    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

}
