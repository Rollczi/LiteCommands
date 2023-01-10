package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistry.IndexKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolverRegistryImpl;
import dev.rollczi.litecommands.modern.command.suggestion.SuggestionResolver;
import dev.rollczi.litecommands.modern.extension.LiteCommandsExtension;
import org.jetbrains.annotations.ApiStatus;

public class LiteCommandsBaseBuilder<SENDER, B extends LiteCommandsBaseBuilder<SENDER, B>> implements LiteCommandsBuilder<SENDER, B>, LiteCommandsInternalBuilderPattern<SENDER> {

    protected final ArgumentResolverRegistry<SENDER> argumentResolverRegistry;
    protected final Class<SENDER> senderClass;
    protected

    LiteCommandsBaseBuilder(Class<SENDER> senderClass) {
        this(
            new ArgumentResolverRegistryImpl<>(),
            senderClass
        );
    }

    protected LiteCommandsBaseBuilder(LiteCommandsInternalBuilderPattern<SENDER> pattern) {
        this(pattern.getArgumentResolver(), pattern.getSenderClass());
    }

    protected LiteCommandsBaseBuilder(
        ArgumentResolverRegistry<SENDER> argumentResolverRegistry,
        Class<SENDER> senderClass
    ) {
        this.argumentResolverRegistry = argumentResolverRegistry;
        this.senderClass = senderClass;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<EXPECTED> type, ARGUMENT argument) {
        IndexKey<Object, EXPECTED, ArgumentContext<Object, EXPECTED>> key = IndexKey.universal(type);

        argumentResolverRegistry.registerResolver(key, argument);
        return this;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<EXPECTED> type, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<Object, EXPECTED, ArgumentContext<Object, EXPECTED>> key = IndexKey.universal(type, argumentKey);

        argumentResolverRegistry.registerResolver(key, argument);
        return this;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argument(Class<EXPECTED> type, ARGUMENT argument) {
        IndexKey<Object, EXPECTED, ArgumentContext<Object, EXPECTED>> key = IndexKey.universal(type);

        argumentResolverRegistry.registerResolver(key, argument);
        return this;
    }

    @Override
    public <EXPECTED, ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>> LiteCommandsBuilder<SENDER, B> argument(Class<EXPECTED> type, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<Object, EXPECTED, ArgumentContext<Object, EXPECTED>> key = IndexKey.universal(type, argumentKey);

        argumentResolverRegistry.registerResolver(key, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContext<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType);

        argumentResolverRegistry.registerResolver(indexKey, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argumentOnly(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContext<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType, argumentKey);

        argumentResolverRegistry.registerResolver(indexKey, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argument(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContext<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType);

        argumentResolverRegistry.registerResolver(indexKey, argument);
        return this;
    }

    @Override
    public <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>, ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> LiteCommandsBuilder<SENDER, B> argument(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ARGUMENT argument, ArgumentKey argumentKey) {
        IndexKey<DETERMINANT, EXPECTED, ArgumentContext<DETERMINANT, EXPECTED>> indexKey = IndexKey.of(determinantType, expectedType, contextType, argumentKey);

        argumentResolverRegistry.registerResolver(indexKey, argument);
        return this;
    }


    @Override
    public <NEW_BUILDER extends LiteCommandsBuilder<SENDER, NEW_BUILDER>> NEW_BUILDER withExtension(LiteCommandsExtension<SENDER, NEW_BUILDER> extension) {
        return  extension.extend(this);
    }

    @Override
    public LiteCommands<SENDER> register() {
        return new LiteCommandsBase<>(); //TODO add other stuff
    }

    @Override
    @ApiStatus.Internal
    public ArgumentResolverRegistry<SENDER> getArgumentResolver() {
        return argumentResolverRegistry;
    }

    @Override
    @ApiStatus.Internal
    public Class<SENDER> getSenderClass() {
        return senderClass;
    }

}
