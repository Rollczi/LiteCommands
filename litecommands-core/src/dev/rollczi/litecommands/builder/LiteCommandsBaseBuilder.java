package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ArgumentParser;
import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistry;
import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistryImpl;
import dev.rollczi.litecommands.argument.parser.ArgumentTypedParser;
import dev.rollczi.litecommands.bind.BindProvider;
import dev.rollczi.litecommands.command.CommandExecuteService;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.LegacyContextProvider;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.builder.extension.LiteCommandsExtension;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPostProcessor;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPreProcessor;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.editor.Editor;
import dev.rollczi.litecommands.exception.ExceptionHandleService;
import dev.rollczi.litecommands.exception.ExceptionHandler;
import dev.rollczi.litecommands.result.ResultHandler;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invalid.InvalidUsageHandler;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import dev.rollczi.litecommands.platform.PlatformSettingsConfigurator;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.scheduler.SchedulerSameThreadImpl;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.suggestion.Suggester;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.SuggestionService;
import dev.rollczi.litecommands.suggestion.TypedSuggester;
import dev.rollczi.litecommands.suggestion.SuggesterRegistry;
import dev.rollczi.litecommands.suggestion.SuggesterRegistryImpl;
import dev.rollczi.litecommands.util.Preconditions;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.LiteCommandsBase;
import dev.rollczi.litecommands.result.ResultService;
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

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LiteCommandsBaseBuilder<SENDER, C extends PlatformSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> implements
    LiteCommandsBuilder<SENDER, C, B>,
    LiteCommandsInternalBuilderApi<SENDER, C> {

    protected final Class<SENDER> senderClass;
    protected final Platform<SENDER, C> platform;

    protected LiteBuilderPreProcessor<SENDER, C> preProcessor;
    protected LiteBuilderPostProcessor<SENDER, C> postProcessor;
    protected Scheduler scheduler;

    protected final EditorService<SENDER> editorService;
    protected final ValidatorService<SENDER> validatorService;
    protected final ArgumentParserRegistry<SENDER> argumentParserRegistry;
    protected final SuggesterRegistry<SENDER> suggesterRegistry;
    protected final BindRegistry<SENDER> bindRegistry;
    protected final ContextRegistry<SENDER> contextRegistry;
    protected final WrapperRegistry wrapperRegistry;
    protected final ResultService<SENDER> resultService;
    protected final ExceptionHandleService<SENDER> exceptionHandleService;
    protected final CommandBuilderCollector<SENDER> commandBuilderCollector;

    /**
     * Simple constructor for api usage
     *
     * @param senderClass class of sender
     */
    public LiteCommandsBaseBuilder(Class<SENDER> senderClass, Platform<SENDER, C> platform) {
        this(
            senderClass,
            platform,
            LiteBuilderPreProcessor.empty(),
            LiteBuilderPostProcessor.empty(),
            new SchedulerSameThreadImpl(),

            new EditorService<>(),
            new ValidatorService<>(),
            new ArgumentParserRegistryImpl<>(),
            new SuggesterRegistryImpl<>(),
            new BindRegistry<>(),
            new ContextRegistry<>(),
            new WrapperRegistry(),
            new ResultService<>(),
            new ExceptionHandleService<>(),
            new CommandBuilderCollector<>()
        );
    }

    /**
     * Pattern constructor for extensions
     *
     * @param pattern pattern to copy
     */
    public LiteCommandsBaseBuilder(LiteCommandsInternalBuilderApi<SENDER, C> pattern) {
        this(
            pattern.getSenderClass(),
            pattern.getPlatform(),
            pattern.getPreProcessor(),
            pattern.getPostProcessor(),
            pattern.getScheduler(),

            pattern.getEditorService(),
            pattern.getValidatorService(),
            pattern.getArgumentParserService(),
            pattern.getSuggesterRegistry(),
            pattern.getBindRegistry(),
            pattern.getContextRegistry(),
            pattern.getWrapperRegistry(),
            pattern.getResultService(),
            pattern.getExceptionHandleService(),
            pattern.getCommandBuilderCollector()
        );
    }

    /**
     * Base constructor
     */
    protected LiteCommandsBaseBuilder(
        Class<SENDER> senderClass,
        Platform<SENDER, C> platform,
        LiteBuilderPreProcessor<SENDER, C> builderProcessor,
        LiteBuilderPostProcessor<SENDER, C> postProcessor,

        Scheduler scheduler,
        EditorService<SENDER> editorService,
        ValidatorService<SENDER> validatorService,
        ArgumentParserRegistry<SENDER> argumentParserRegistry,
        SuggesterRegistry<SENDER> suggesterRegistry,
        BindRegistry<SENDER> bindRegistry,
        ContextRegistry<SENDER> contextRegistry,
        WrapperRegistry wrapperRegistry,
        ResultService<SENDER> resultService,
        ExceptionHandleService<SENDER> exceptionHandleService,
        CommandBuilderCollector<SENDER> commandBuilderCollector
    ) {
        this.senderClass = senderClass;
        this.platform = platform;
        this.preProcessor = builderProcessor;
        this.postProcessor = postProcessor;

        this.scheduler = scheduler;
        this.editorService = editorService;
        this.validatorService = validatorService;
        this.argumentParserRegistry = argumentParserRegistry;
        this.suggesterRegistry = suggesterRegistry;
        this.bindRegistry = bindRegistry;
        this.contextRegistry = contextRegistry;
        this.wrapperRegistry = wrapperRegistry;
        this.resultService = resultService;
        this.exceptionHandleService = exceptionHandleService;
        this.commandBuilderCollector = commandBuilderCollector;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> platformSettings(PlatformSettingsConfigurator<C> configurator) {
        C newConfig = configurator.apply(this.platform.getConfiguration());
        Preconditions.notNull(newConfig, "configuration");

        this.platform.setConfiguration(newConfig);

        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> commands(LiteCommandsProvider<SENDER> commandsProvider) {
        this.commandBuilderCollector.add(commandsProvider.toRealProvider(this));
        return this;
    }

    @Override
    public <IN, T, PARSER extends ArgumentParser<SENDER, IN, T>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<T> type, PARSER parser) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.of(), parser);
        return this;
    }

    @Override
    public <IN, PARSED, PARSER extends ArgumentParser<SENDER, IN, PARSED>>
    LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, String key, PARSER parser) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.of(key), parser);
        return this;
    }

    @Override
    public <IN, T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<T> type, ArgumentTypedParser<SENDER, IN, T, ARGUMENT> parser) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.typed(parser.getArgumentType()), parser);
        return this;
    }

    @Override
    public <IN, T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<T> type, String key, ArgumentTypedParser<SENDER, IN, T, ARGUMENT> parser) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.typed(parser.getArgumentType(), key), parser);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER, C, B> argumentSuggester(Class<T> type, SuggestionResult suggestionResult) {
        this.suggesterRegistry.registerSuggester(type, ArgumentKey.of(), (invocation, argument, context) -> suggestionResult);
        return this;
    }

    @Override
    public <T, SUGGESTER extends Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, C, B> argumentSuggester(Class<T> type, SUGGESTER suggester) {
        this.suggesterRegistry.registerSuggester(type, ArgumentKey.of(), suggester);
        return this;
    }

    @Override
    public <T, SUGGESTER extends Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, C, B> argumentSuggester(Class<T> type, String key, SUGGESTER suggester) {
        this.suggesterRegistry.registerSuggester(type, ArgumentKey.of(key), suggester);
        return this;
    }

    @Override
    public <T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, C, B> argumentSuggester(Class<T> type, TypedSuggester<SENDER, T, ARGUMENT> suggester) {
        this.suggesterRegistry.registerSuggester(type, ArgumentKey.typed(suggester.getArgumentType()), suggester);
        return this;
    }

    @Override
    public <T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, C, B> argumentSuggester(Class<T> type, String key, TypedSuggester<SENDER, T, ARGUMENT> suggester) {
        this.suggesterRegistry.registerSuggester(type, ArgumentKey.typed(suggester.getArgumentType(), key), suggester);
        return this;
    }

    @Override
    public <IN, T, RESOLVER extends ArgumentParser<SENDER, IN, T> & Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, C, B> argument(Class<T> type, RESOLVER resolver) {
        this.argumentParser(type, resolver);
        this.argumentSuggester(type, resolver);
        return this;
    }

    @Override
    public <IN, PARSED, RESOLVER extends ArgumentParser<SENDER, IN, PARSED> & Suggester<SENDER, PARSED>>
    LiteCommandsBuilder<SENDER, C, B> argument(Class<PARSED> type, String key, RESOLVER resolver) {
        this.argumentParser(type, key, resolver);
        this.argumentSuggester(type, key, resolver);
        return this;
    }

    @Override
    public <IN, T, ARGUMENT extends Argument<T>, RESOLVER extends ArgumentTypedParser<SENDER, IN, T, ARGUMENT> & Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, C, B> argument(Class<T> type, RESOLVER resolver) {
        this.argumentParser(type, resolver);
        this.argumentSuggester(type, resolver);
        return this;
    }

    @Override
    public <IN, T, ARGUMENT extends Argument<T>, RESOLVER extends ArgumentTypedParser<SENDER, IN, T, ARGUMENT> & Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, C, B> argument(Class<T> type, String key, RESOLVER resolver) {
        this.argumentParser(type, key, resolver);
        this.argumentSuggester(type, key, resolver);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER, C, B> context(Class<T> on, ContextProvider<SENDER, T> bind) {
        this.contextRegistry.registerProvider(on, bind);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER, C, B> bind(Class<T> on, BindProvider<T> bindProvider) {
        this.bindRegistry.bindInstance(on, bindProvider);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER, C, B> bind(Class<T> on, Supplier<T> bind) {
        this.bindRegistry.bindInstance(on, bind);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> bindUnsafe(Class<?> on, Supplier<?> bind) {
        this.bindRegistry.bindInstanceUnsafe(on, bind);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> scheduler(Scheduler scheduler) {
        this.scheduler.shutdown();
        this.scheduler = scheduler;
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> editorGlobal(Editor<SENDER> editor) {
        this.editorService.editorGlobal(editor);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> editor(Scope scope, Editor<SENDER> editor) {
        this.editorService.editor(scope, editor);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> validatorGlobal(Validator<SENDER> validator) {
        this.validatorService.registerValidatorGlobal(validator);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> validator(Scope scope, Validator<SENDER> validator) {
        this.validatorService.registerValidator(scope, validator);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER, C, B> result(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler) {
        this.resultService.registerHandler(resultType, handler);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> resultUnexpected(ResultHandler<SENDER, Object> handler) {
        this.resultService.registerHandler(Object.class, handler);
        return this;
    }

    @Override
    public <E extends Throwable> LiteCommandsBuilder<SENDER, C, B> exception(Class<E> exceptionType, ExceptionHandler<SENDER, ? extends E> handler) {
        this.exceptionHandleService.registerHandler(exceptionType, handler);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> exceptionUnexpected(ExceptionHandler<SENDER, Throwable> handler) {
        this.exceptionHandleService.registerUnexpectedHandler(handler);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> missingPermission(MissingPermissionsHandler<SENDER> handler) {
        this.resultService.registerHandler(MissingPermissions.class, handler);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> invalidUsage(InvalidUsageHandler<SENDER> handler) {
        this.resultService.registerHandler(InvalidUsage.class, handler);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> wrapper(Wrapper wrapper) {
        this.wrapperRegistry.registerFactory(wrapper);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> preProcessor(LiteBuilderPreProcessor<SENDER, C> preProcessor) {
        this.preProcessor = preProcessor;
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> postProcessor(LiteBuilderPostProcessor<SENDER, C> postProcessor) {
        this.postProcessor = postProcessor;
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> extension(LiteCommandsExtension<SENDER> extension) {
        extension.extend(this, this);
        return this;
    }

    @Override
    public <E extends LiteCommandsExtension<SENDER>> LiteCommandsBuilder<SENDER, C, B> extension(E extension, UnaryOperator<E> configuration) {
        extension = configuration.apply(extension);
        extension.extend(this, this);
        return this;
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

        CommandExecuteService<SENDER> commandExecuteService = new CommandExecuteService<>(validatorService, resultService, exceptionHandleService, scheduler);
        SuggestionService<SENDER> suggestionService = new SuggestionService<>(argumentParserRegistry, suggesterRegistry, validatorService);
        CommandManager<SENDER, C> commandManager = new CommandManager<>(this.platform, commandExecuteService, suggestionService);

        List<CommandBuilder<SENDER>> collectedContexts = this.commandBuilderCollector.collectAndMergeCommands();

        for (CommandBuilder<SENDER> context : collectedContexts) {
            if (!context.buildable()) {
                continue;
            }

            for (CommandRoute<SENDER> commandRoute : context.build(commandManager.getRoot())) {
                commandManager.register(commandRoute);
            }
        }

        LiteCommandsBase<SENDER> liteCommand = new LiteCommandsBase<>(commandManager);

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
    public LiteBuilderPreProcessor<SENDER, C> getPreProcessor() {
        return this.preProcessor;
    }

    @Override
    public LiteBuilderPostProcessor<SENDER, C> getPostProcessor() {
        return this.postProcessor;
    }

    @Override
    @ApiStatus.Internal
    public Scheduler getScheduler() {
        return this.scheduler;
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
    public ArgumentParserRegistry<SENDER> getArgumentParserService() {
        return this.argumentParserRegistry;
    }

    @Override
    @ApiStatus.Internal
    public SuggesterRegistry<SENDER> getSuggesterRegistry() {
        return this.suggesterRegistry;
    }

    @Override
    @ApiStatus.Internal
    public BindRegistry<SENDER> getBindRegistry() {
        return this.bindRegistry;
    }

    @Override
    @ApiStatus.Internal
    public ContextRegistry<SENDER> getContextRegistry() {
        return this.contextRegistry;
    }

    @Override
    @ApiStatus.Internal
    public WrapperRegistry getWrapperRegistry() {
        return this.wrapperRegistry;
    }

    @Override
    @ApiStatus.Internal
    public ResultService<SENDER> getResultService() {
        return this.resultService;
    }

    @Override
    @ApiStatus.Internal
    public CommandBuilderCollector<SENDER> getCommandBuilderCollector() {
        return this.commandBuilderCollector;
    }

    @Override
    public ExceptionHandleService<SENDER> getExceptionHandleService() {
        return this.exceptionHandleService;
    }

}
