package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.argument.ArgumentResolver;
import dev.rollczi.litecommands.modern.argument.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.bind.Bind;
import dev.rollczi.litecommands.modern.bind.BindContextual;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultMapper;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.command.CommandManager;
import dev.rollczi.litecommands.modern.command.editor.CommandEditor;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.command.filter.CommandFilter;
import dev.rollczi.litecommands.modern.command.filter.CommandFilterService;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualFactory;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.platform.Platform;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LiteCommandsBaseBuilder<SENDER, B extends LiteCommandsBaseBuilder<SENDER, B>> implements LiteCommandsBuilder<SENDER, B>, LiteCommandsInternalPattern<SENDER> {

    protected final Class<SENDER> senderClass;

    protected final CommandEditorService commandEditorService;
    protected final CommandFilterService<SENDER> commandFilterService;
    protected final ArgumentService<SENDER> argumentService;
    protected final BindRegistry<SENDER> bindRegistry;
    protected final WrappedExpectedContextualService wrappedExpectedContextualService;
    protected final CommandExecuteResultResolver<SENDER> resultResolver;

    protected final CommandEditorContextRegistry commandEditorContextRegistry;

    protected @Nullable Platform<SENDER> platform;

    /**
     * Simple constructor for api usage
     *
     * @param senderClass class of sender
     */
    public LiteCommandsBaseBuilder(Class<SENDER> senderClass) {
        this(senderClass, null);
    }

    /**
     * Simple constructor for api usage
     *
     * @param senderClass class of sender
     */
    public LiteCommandsBaseBuilder(Class<SENDER> senderClass, Platform<SENDER> platform) {
        this(
            senderClass,
            new CommandEditorService(),
            new CommandFilterService<>(),
            new ArgumentService<>(),
            new BindRegistry<>(),
            new WrappedExpectedContextualService(),
            new CommandExecuteResultResolver<>(),
            new CommandEditorContextRegistry(),
            platform
        );
    }

    /**
     * Pattern constructor for extensions
     *
     * @param pattern pattern to copy
     */
    public LiteCommandsBaseBuilder(LiteCommandsInternalPattern<SENDER> pattern) {
        this(
            pattern.getSenderClass(),
            pattern.getCommandEditorService(),
            pattern.getCommandFilterService(),
            pattern.getArgumentService(),
            pattern.getBindRegistry(),
            pattern.getWrappedExpectedContextualService(),
            pattern.getResultResolver(),
            pattern.getCommandContextRegistry(),
            pattern.getPlatform()
        );
    }

    /**
     * Base constructor
     */
    protected LiteCommandsBaseBuilder(
        Class<SENDER> senderClass,
        CommandEditorService commandEditorService,
        CommandFilterService<SENDER> commandFilterService, ArgumentService<SENDER> argumentService,
        BindRegistry<SENDER> bindRegistry,
        WrappedExpectedContextualService wrappedExpectedContextualService,
        CommandExecuteResultResolver<SENDER> resultResolver,
        CommandEditorContextRegistry commandEditorContextRegistry,
        @Nullable Platform<SENDER> platform
    ) {
        this.senderClass = senderClass;
        this.commandEditorService = commandEditorService;
        this.commandFilterService = commandFilterService;
        this.argumentService = argumentService;
        this.bindRegistry = bindRegistry;
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
        this.resultResolver = resultResolver;
        this.commandEditorContextRegistry = commandEditorContextRegistry;
        this.platform = platform;
    }

    @Override
    public B editor(String command, CommandEditor commandEditor) {
        this.commandEditorService.registerEditor(command, commandEditor);
        return this.getThis();
    }

    @Override
    public B globalEditor(CommandEditor commandEditor) {
        this.commandEditorService.registerGlobalEditor(commandEditor);
        return this.getThis();
    }

    @Override
    public B filter(CommandFilter<SENDER> filter) {
        this.commandFilterService.registerFilter(filter);
        return this.getThis();
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> B argumentOnly(Class<EXPECTED> type, ARGUMENT argument) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type);

        this.argumentService.registerResolver(key, argument);
        return this.getThis();
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> B argumentOnly(Class<EXPECTED> type, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type, argumentKey);

        this.argumentService.registerResolver(key, argument);
        return this.getThis();
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> B argument(Class<EXPECTED> type, ARGUMENT argument) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type);

        this.argumentService.registerResolver(key, argument);
        return this.getThis();
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> B argument(Class<EXPECTED> type, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type, argumentKey);

        this.argumentService.registerResolver(key, argument);
        return this.getThis();
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> B argumentOnly(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType);

        this.argumentService.registerResolver(indexKey, argument);
        return this.getThis();
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> B argumentOnly(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType, argumentKey);

        this.argumentService.registerResolver(indexKey, argument);
        return this.getThis();
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> B argument(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType);

        this.argumentService.registerResolver(indexKey, argument);
        return this.getThis();
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> B argument(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType, argumentKey);

        this.argumentService.registerResolver(indexKey, argument);
        return this.getThis();
    }

    @Override
    public <T> B typeBind(Class<T> on, Bind<T> bind) {
        this.bindRegistry.bindInstance(on, bind);
        return this.getThis();
    }

    @Override
    public <T> B typeBind(Class<T> on, Supplier<T> bind) {
        this.bindRegistry.bindInstance(on, bind);
        return this.getThis();
    }

    @Override
    public B typeBindUnsafe(Class<?> on, Supplier<?> bind) {
        this.bindRegistry.bindInstanceUnsafe(on, bind);
        return this.getThis();
    }

    @Override
    public <T> B contextualBind(Class<T> on, BindContextual<SENDER, T> bind) {
        this.bindRegistry.bindContextual(on, bind);
        return this.getThis();
    }

    @Override
    public B wrappedExpectedContextualFactory(WrappedExpectedContextualFactory factory) {
        this.wrappedExpectedContextualService.registerFactory(factory);
        return this.getThis();
    }

    @Override
    public <T> B resultHandler(Class<T> resultType, CommandExecuteResultHandler<SENDER, T> handler) {
        this.resultResolver.registerHandler(resultType, handler);
        return this.getThis();
    }

    @Override
    public <T> B resultMapper(Class<T> mapperFromType, CommandExecuteResultMapper<SENDER, T, ?> mapper) {
        this.resultResolver.registerMapper(mapperFromType, mapper);
        return this.getThis();
    }

    @Override
    public <E extends LiteExtension<SENDER>> B withExtension(E extension) {
        extension.extend(this.getThis());
        return this.getThis();
    }

    @Override
    public <E extends LiteExtension<SENDER>> B withExtension(E extension, UnaryOperator<E> configuration) {
        extension = configuration.apply(extension);
        extension.extend(this.getThis());
        return this.getThis();
    }

    @Override
    public B platform(Platform<SENDER> platform) {
        this.platform = platform;
        return this.getThis();
    }

    @Override
    public LiteCommands<SENDER> register() {
        CommandManager<SENDER> commandManager = new CommandManager<>(
            this.platform,
            this.wrappedExpectedContextualService,
            this.argumentService,
            this.resultResolver,
            this.bindRegistry,
            this.commandFilterService
        );

        return new LiteCommandsBase<>(commandManager); //TODO add other stuff
    }

    @Override
    @ApiStatus.Internal
    public Class<SENDER> getSenderClass() {
        return this.senderClass;
    }

    @Override
    @ApiStatus.Internal
    public CommandEditorService getCommandEditorService() {
        return this.commandEditorService;
    }

    @Override
    public CommandFilterService<SENDER> getCommandFilterService() {
        return this.commandFilterService;
    }

    @Override
    @ApiStatus.Internal
    public ArgumentService<SENDER> getArgumentService() {
        return this.argumentService;
    }

    @Override
    public BindRegistry<SENDER> getBindRegistry() {
        return this.bindRegistry;
    }

    @Override
    @ApiStatus.Internal
    public WrappedExpectedContextualService getWrappedExpectedContextualService() {
        return this.wrappedExpectedContextualService;
    }

    @Override
    @ApiStatus.Internal
    public CommandExecuteResultResolver<SENDER> getResultResolver() {
        return this.resultResolver;
    }

    @Override
    public CommandEditorContextRegistry getCommandContextRegistry() {
        return this.commandEditorContextRegistry;
    }

    @Override
    @Nullable
    @ApiStatus.Internal
    public Platform<SENDER> getPlatform() {
        return this.platform;
    }

    @SuppressWarnings("unchecked")
    protected B getThis() {
        return (B) this;
    }

}
