package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.command.CommandManager;
import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentService;
import dev.rollczi.litecommands.modern.command.contextual.warpped.WrappedArgumentService;
import dev.rollczi.litecommands.modern.command.suggestion.SuggestionResolver;
import dev.rollczi.litecommands.modern.extension.LiteCommandsExtension;
import dev.rollczi.litecommands.modern.extension.annotation.inject.InjectBindRegistry;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiteCommandsBaseBuilder<SENDER, B extends LiteCommandsBaseBuilder<SENDER, B>> implements LiteCommandsBuilder<SENDER, B>, LiteCommandsInternalBuilderPattern<SENDER> {

    protected final Class<SENDER> senderClass;

    protected final ArgumentService<SENDER> argumentService;
    protected final CommandExecuteResultResolver<SENDER> resultResolver;
    protected final WrappedArgumentService wrappedArgumentService;

    protected @Nullable Platform<SENDER> platform;

    /**
     * Simple constructor for api usage
     *
     * @param senderClass class of sender
     */
    public LiteCommandsBaseBuilder(Class<SENDER> senderClass) {
        this(
            senderClass,
            new ArgumentService<>(),
            new CommandExecuteResultResolver<>(),
            new WrappedArgumentService(),
            null
        );
    }

    /**
     * Simple constructor for api usage
     *
     * @param senderClass class of sender
     */
    public LiteCommandsBaseBuilder(Class<SENDER> senderClass, @NotNull Platform<SENDER> platform) {
        this(
            senderClass,
            new ArgumentService<>(),
            new CommandExecuteResultResolver<>(),
            new WrappedArgumentService(),
            platform
        );
    }

    /**
     * Pattern constructor for extensions
     *
     * @param pattern pattern to copy
     */
    protected LiteCommandsBaseBuilder(LiteCommandsInternalBuilderPattern<SENDER> pattern) {
        this(pattern.getSenderClass(), pattern.getArgumentService(), pattern.getResultResolver(), pattern.getWrappedArgumentService(), pattern.getPlatform());
    }

    /**
     * Base constructor
     *
     * @param senderClass            class of sender
     * @param argumentService        argument service
     * @param resultResolver         result resolver
     * @param wrappedArgumentService wrapped argument service
     */
    private LiteCommandsBaseBuilder(
        Class<SENDER> senderClass,
        ArgumentService<SENDER> argumentService,
        CommandExecuteResultResolver<SENDER> resultResolver,
        WrappedArgumentService wrappedArgumentService,
        @Nullable Platform<SENDER> platform
    ) {
        this.senderClass = senderClass;
        this.argumentService = argumentService;
        this.resultResolver = resultResolver;
        this.wrappedArgumentService = wrappedArgumentService;
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
    public <NEW_BUILDER extends LiteCommandsBuilder<SENDER, NEW_BUILDER>> NEW_BUILDER withExtension(LiteCommandsExtension<SENDER, NEW_BUILDER> extension) {
        return extension.extend(this);
    }

    @Override
    public LiteCommandsBuilder<SENDER, B> platform(Platform<SENDER> platform) {
        this.platform = platform;
        return this;
    }

    @Override
    public LiteCommands<SENDER> register() {
        CommandManager<SENDER> commandManager = new CommandManager<>(this.wrappedArgumentService, this.argumentService, this.platform, this.resultResolver, new InjectBindRegistry<>());


        return new LiteCommandsBase<>(); //TODO add other stuff
    }

    @Override
    @ApiStatus.Internal
    public Class<SENDER> getSenderClass() {
        return this.senderClass;
    }

    @Override
    @ApiStatus.Internal
    public ArgumentService<SENDER> getArgumentService() {
        return this.argumentService;
    }

    @Override
    @ApiStatus.Internal
    public CommandExecuteResultResolver<SENDER> getResultResolver() {
        return this.resultResolver;
    }

    @Override
    @ApiStatus.Internal
    public WrappedArgumentService getWrappedArgumentService() {
        return this.wrappedArgumentService;
    }

    @Override
    @Nullable
    @ApiStatus.Internal
    public Platform<SENDER> getPlatform() {
        return this.platform;
    }

}
