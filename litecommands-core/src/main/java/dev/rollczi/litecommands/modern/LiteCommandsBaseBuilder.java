package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.command.CommandManager;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedExpectedContextualService;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.command.suggestion.SuggestionResolver;
import dev.rollczi.litecommands.modern.extension.LiteExtension;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public class LiteCommandsBaseBuilder<SENDER, B extends LiteCommandsBaseBuilder<SENDER, B>> implements LiteCommandsBuilder<SENDER, B>, LiteCommandsInternalPattern<SENDER> {

    protected final Class<SENDER> senderClass;

    protected final CommandEditorService commandEditorService;
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
    protected LiteCommandsBaseBuilder(LiteCommandsInternalPattern<SENDER> pattern) {
        this(
            pattern.getSenderClass(),
            pattern.getCommandEditorService(),
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
        ArgumentService<SENDER> argumentService,
        BindRegistry<SENDER> bindRegistry,
        WrappedExpectedContextualService wrappedExpectedContextualService,
        CommandExecuteResultResolver<SENDER> resultResolver,
        CommandEditorContextRegistry commandEditorContextRegistry,
        @Nullable Platform<SENDER> platform
    ) {
        this.senderClass = senderClass;
        this.commandEditorService = commandEditorService;
        this.argumentService = argumentService;
        this.bindRegistry = bindRegistry;
        this.wrappedExpectedContextualService = wrappedExpectedContextualService;
        this.resultResolver = resultResolver;
        this.commandEditorContextRegistry = commandEditorContextRegistry;
        this.platform = platform;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<EXPECTED> type, ARGUMENT argument) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type);

        this.argumentService.registerResolver(key, argument);
        return this;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<EXPECTED> type, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type, argumentKey);

        this.argumentService.registerResolver(key, argument);
        return this;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argument(Class<EXPECTED> type, ARGUMENT argument) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type);

        this.argumentService.registerResolver(key, argument);
        return this;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContextual<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argument(Class<EXPECTED> type, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<Object, EXPECTED, ArgumentContextual<Object, EXPECTED>> key = IndexKey.universal(type, argumentKey);

        this.argumentService.registerResolver(key, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType);

        this.argumentService.registerResolver(indexKey, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType, argumentKey);

        this.argumentService.registerResolver(indexKey, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argument(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType);

        this.argumentService.registerResolver(indexKey, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContextual<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argument(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContextual<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType, argumentKey);

        this.argumentService.registerResolver(indexKey, argument);
        return this;
    }

    @Override
    public <E extends LiteExtension<SENDER>> LiteCommandsBuilder<SENDER, B> withExtension(E extension) {
        extension.extend((B) this);
        return this;
    }

    @Override
    public <E extends LiteExtension<SENDER>> LiteCommandsBuilder<SENDER, B> withExtension(E extension, UnaryOperator<E> configuration) {
        extension = configuration.apply(extension);
        extension.extend((B) this);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER, B> platform(Platform<SENDER> platform) {
        this.platform = platform;
        return this;
    }

    @Override
    public LiteCommands<SENDER> register() {
        CommandManager<SENDER> commandManager = new CommandManager<>(
            this.wrappedExpectedContextualService,
            this.argumentService,
            this.platform,
            this.resultResolver,
            this.bindRegistry
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

}
