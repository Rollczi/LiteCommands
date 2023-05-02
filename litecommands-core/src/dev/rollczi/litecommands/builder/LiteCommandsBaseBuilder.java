package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.ArgumentParser;
import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistry;
import dev.rollczi.litecommands.bind.Bind;
import dev.rollczi.litecommands.bind.BindContextual;
import dev.rollczi.litecommands.bind.BindRegistry;
import dev.rollczi.litecommands.builder.extension.LiteCommandsExtension;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPostProcessor;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPreProcessor;
import dev.rollczi.litecommands.util.Preconditions;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.LiteCommandsBase;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.command.CommandExecuteResultMapper;
import dev.rollczi.litecommands.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.command.CommandManager;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.editor.CommandEditor;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.editor.CommandEditorService;
import dev.rollczi.litecommands.platform.LiteSettings;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorScope;
import dev.rollczi.litecommands.validator.ValidatorService;
import dev.rollczi.litecommands.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LiteCommandsBaseBuilder<SENDER, C extends LiteSettings, B extends LiteCommandsBaseBuilder<SENDER, C, B>> implements
    LiteCommandsBuilder<SENDER, C, B>,
    LiteCommandsInternalBuilderApi<SENDER, C> {

    protected final Class<SENDER> senderClass;
    protected final Platform<SENDER, C> platform;

    protected LiteBuilderPreProcessor<SENDER, C> preProcessor;
    protected LiteBuilderPostProcessor<SENDER, C> postProcessor;

    protected final CommandEditorService<SENDER> commandEditorService;
    protected final ValidatorService<SENDER> validatorService;
    protected final ArgumentParserRegistry<SENDER> argumentParserRegistry;
    protected final BindRegistry<SENDER> bindRegistry;
    protected final WrappedExpectedService wrappedExpectedService;
    protected final CommandExecuteResultResolver<SENDER> resultResolver;
    protected final CommandEditorContextRegistry<SENDER> commandEditorContextRegistry;

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
            new CommandEditorService<>(),
            new ValidatorService<>(),
            ArgumentParserRegistry.createRegistry(),
            new BindRegistry<>(),
            new WrappedExpectedService(),
            new CommandExecuteResultResolver<>(),
            new CommandEditorContextRegistry<>()
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
            pattern.getCommandEditorService(),
            pattern.getCommandFilterService(),
            pattern.getArgumentService(),
            pattern.getBindRegistry(),
            pattern.getWrappedExpectedContextualService(),
            pattern.getResultResolver(),
            pattern.getCommandContextRegistry()
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
        CommandEditorService<SENDER> commandEditorService,
        ValidatorService<SENDER> validatorService, ArgumentParserRegistry<SENDER> argumentParserRegistry,
        BindRegistry<SENDER> bindRegistry,
        WrappedExpectedService wrappedExpectedService,
        CommandExecuteResultResolver<SENDER> resultResolver,
        CommandEditorContextRegistry<SENDER> commandEditorContextRegistry
    ) {
        this.senderClass = senderClass;
        this.platform = platform;
        this.preProcessor = builderProcessor;
        this.postProcessor = postProcessor;
        this.commandEditorService = commandEditorService;
        this.validatorService = validatorService;
        this.argumentParserRegistry = argumentParserRegistry;
        this.bindRegistry = bindRegistry;
        this.wrappedExpectedService = wrappedExpectedService;
        this.resultResolver = resultResolver;
        this.commandEditorContextRegistry = commandEditorContextRegistry;
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> applySettings(UnaryOperator<C> operator) {
        C newConfig = operator.apply(this.platform.getConfiguration());
        Preconditions.notNull(newConfig, "configuration");

        this.platform.setConfiguration(newConfig);

        return this.getThis();
    }

    @Override
    public B withEditor(String command, CommandEditor<SENDER> commandEditor) {
        this.commandEditorService.registerEditor(command, commandEditor);
        return this.getThis();
    }

    @Override
    public B withGlobalEditor(CommandEditor<SENDER> commandEditor) {
        this.commandEditorService.registerGlobalEditor(commandEditor);
        return this.getThis();
    }

    @Override
    public B withValidator(Validator<SENDER> validator) {
        this.validatorService.registerValidator(validator, ValidatorScope.GLOBAL);
        return this.getThis();
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> withValidator(Validator<SENDER> validator, ValidatorScope scope) {
        this.validatorService.registerValidator(validator, scope);
        return this;
    }

    @Override
    public <INPUT, PARSED, PARSER extends ArgumentParser<SENDER, INPUT, PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, PARSER parser) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.universal(), parser);
        return this;
    }

    @Override
    public <INPUT, PARSED, PARSER extends ArgumentParser<SENDER, INPUT, PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, String key, PARSER parser) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.universal(key), parser);
        return this;
    }

    @Override
    public <INPUT, PARSED, PARSER extends Argument<PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, Class<PARSER> argumentType, ArgumentParser<SENDER, INPUT, PARSED> resolver) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.key(argumentType), resolver);
        return this;
    }

    @Override
    public <INPUT, PARSED, PARSER extends Argument<PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, Class<PARSER> argumentType, String key, ArgumentParser<SENDER, INPUT, PARSED> resolver) {
        this.argumentParserRegistry.registerParser(type, ArgumentKey.key(argumentType, key), resolver);
        return this;
    }

    @Override
    public <T> B bindStatic(Class<T> on, Bind<T> bind) {
        this.bindRegistry.bindInstance(on, bind);
        return this.getThis();
    }

    @Override
    public <T> B bindStatic(Class<T> on, Supplier<T> bind) {
        this.bindRegistry.bindInstance(on, bind);
        return this.getThis();
    }

    @Override
    public B bindStaticUnsafe(Class<?> on, Supplier<?> bind) {
        this.bindRegistry.bindInstanceUnsafe(on, bind);
        return this.getThis();
    }

    @Override
    public <T> B bindContext(Class<T> on, BindContextual<SENDER, T> bind) {
        this.bindRegistry.bindContextual(on, bind);
        return this.getThis();
    }

    @Override
    public B registerWrapperFactory(WrappedExpectedFactory factory) {
        this.wrappedExpectedService.registerFactory(factory);
        return this.getThis();
    }

    @Override
    public <T> B resultHandler(Class<T> resultType, CommandExecuteResultHandler<SENDER, ? extends T> handler) {
        this.resultResolver.registerHandler(resultType, handler);
        return this.getThis();
    }

    @Override
    public <T> B resultMapper(Class<T> mapperFromType, CommandExecuteResultMapper<SENDER, T, ?> mapper) {
        this.resultResolver.registerMapper(mapperFromType, mapper);
        return this.getThis();
    }

    @Override
    public <E extends LiteCommandsExtension<SENDER>> B withExtension(E extension) {
        extension.extend(this, this);
        return this.getThis();
    }

    @Override
    public <E extends LiteCommandsExtension<SENDER>> B withExtension(E extension, UnaryOperator<E> configuration) {
        extension = configuration.apply(extension);
        extension.extend(this, this);
        return this.getThis();
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> preProcessor(LiteBuilderPreProcessor<SENDER, C> preProcessor) {
        this.preProcessor = preProcessor;
        return this.getThis();
    }

    @Override
    public LiteCommandsBuilder<SENDER, C, B> postProcessor(LiteBuilderPostProcessor<SENDER, C> postProcessor) {
        this.postProcessor = postProcessor;
        return this.getThis();
    }

    @Override
    public LiteCommands<SENDER> register() {
        if (this.platform == null) {
            throw new IllegalStateException("No platform was set");
        }

        CommandManager<SENDER, C> commandManager = new CommandManager<>(
            this.platform,
            this.resultResolver,
            this.validatorService
        );

        List<CommandEditorContext<SENDER>> collectedContexts = this.commandEditorContextRegistry.extractAndMergeContexts();

        for (CommandEditorContext<SENDER> context : collectedContexts) {
            if (!context.buildable()) {
                continue;
            }

            for (CommandRoute<SENDER> commandRoute : context.build(commandManager.getRoot())) {
                commandManager.register(commandRoute);
            }
        }

        return new LiteCommandsBase<>(commandManager);
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
    public CommandEditorService<SENDER> getCommandEditorService() {
        return this.commandEditorService;
    }

    @Override
    @ApiStatus.Internal
    public ValidatorService<SENDER> getCommandFilterService() {
        return this.validatorService;
    }

    @Override
    @ApiStatus.Internal
    public ArgumentParserRegistry<SENDER> getArgumentService() {
        return this.argumentParserRegistry;
    }

    @Override
    @ApiStatus.Internal
    public BindRegistry<SENDER> getBindRegistry() {
        return this.bindRegistry;
    }

    @Override
    @ApiStatus.Internal
    public WrappedExpectedService getWrappedExpectedContextualService() {
        return this.wrappedExpectedService;
    }

    @Override
    @ApiStatus.Internal
    public CommandExecuteResultResolver<SENDER> getResultResolver() {
        return this.resultResolver;
    }

    @Override
    @ApiStatus.Internal
    public CommandEditorContextRegistry<SENDER> getCommandContextRegistry() {
        return this.commandEditorContextRegistry;
    }

    @SuppressWarnings("unchecked")
    protected B getThis() {
        return (B) this;
    }

}
