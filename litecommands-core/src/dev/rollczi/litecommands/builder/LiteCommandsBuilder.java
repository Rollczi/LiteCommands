package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.TypedParser;
import dev.rollczi.litecommands.bind.BindProvider;
import dev.rollczi.litecommands.builder.processor.LiteBuilderProcessor;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.builder.extension.LiteExtension;
import dev.rollczi.litecommands.editor.Editor;
import dev.rollczi.litecommands.handler.exception.ExceptionHandler;
import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.message.Message;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSettingsConfigurator;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.argument.suggester.TypedSuggester;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorScope;
import dev.rollczi.litecommands.wrapper.Wrapper;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Builder for {@link LiteCommands}.
 * Use {@link dev.rollczi.litecommands.LiteCommandsFactory#builder(Class, Platform)} to create new instance of builder.
 *
 * @param <SENDER> type of sender
 * @param <SETTINGS> type of platform settings
 * @param <B> self type for builder
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface LiteCommandsBuilder<SENDER, SETTINGS extends PlatformSettings, B extends LiteCommandsBuilder<SENDER, SETTINGS, B>> {

    /**
     * Configure platform settings.
     *
     * @see PlatformSettings
     * @param configurator configurator for platform settings
     * @return this builder
     */
    LiteCommandsBuilder<SENDER, SETTINGS, B> settings(PlatformSettingsConfigurator<SETTINGS> configurator);

    /**
     * Register commands from given provider.
     *
     * @see LiteCommandsProvider
     * @param commandsProvider provider of commands
     * @return this builder
     */
    LiteCommandsBuilder<SENDER, SETTINGS, B> commands(LiteCommandsProvider<SENDER> commandsProvider);

    <IN, T, PARSER extends Parser<SENDER, IN, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentParser(Class<T> type, PARSER parser);

    <IN, T, PARSER extends Parser<SENDER, IN, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentParser(Class<T> type, ArgumentKey key, PARSER parser);

    <IN, T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentParser(Class<T> type, TypedParser<SENDER, IN, T, ARGUMENT> parser);

    <IN, T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentParser(Class<T> type, ArgumentKey key, TypedParser<SENDER, IN, T, ARGUMENT> parser);

    <T>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentSuggester(Class<T> type, SuggestionResult suggestionResult);

    <T>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentSuggester(Class<T> type, ArgumentKey key, SuggestionResult suggestionResult);

    <T, SUGGESTER extends Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentSuggester(Class<T> type, SUGGESTER suggester);

    <T, SUGGESTER extends Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentSuggester(Class<T> type, ArgumentKey key, SUGGESTER suggester);

    <T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentSuggester(Class<T> type, TypedSuggester<SENDER, T, ARGUMENT> suggester);

    <T, ARGUMENT extends Argument<T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argumentSuggester(Class<T> type, ArgumentKey key, TypedSuggester<SENDER, T, ARGUMENT> suggester);

    /**
     * [Argument Parser and Suggester]
     * Register argument parser and suggester for given type.
     *
     * @param type type of argument
     * @param resolver parser and suggester for given type
     * @return this builder
     */
    <IN, T, RESOLVER extends Parser<SENDER, IN, T> & Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argument(Class<T> type, RESOLVER resolver);

    /**
     * [Keyed Argument Parser and Suggester]
     * Register argument parser and suggester for given type and key.
     * Key is used to define namespace for given parser. It is used to identify which parsers with same type should be used.
     * Argument type is used to define what type is compatible with given parser.
     *
     * @param type type of argument
     * @param key key of argument
     * @param resolver parser and suggester for given type
     * @return this builder
     */
    <IN, T, RESOLVER extends Parser<SENDER, IN, T> & Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argument(Class<T> type, ArgumentKey key, RESOLVER resolver);

    /**
     * [Typed Argument Parser and Suggester]
     * Register argument parser and suggester for given type.
     *
     * @param type type of argument
     * @param resolver parser and suggester for given type
     * @return this builder
     */
    <IN, T, ARGUMENT extends Argument<T>, RESOLVER extends TypedParser<SENDER, IN, T, ARGUMENT> & Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argument(Class<T> type, RESOLVER resolver);

    /**
     * [Typed Keyed Argument Parser and Suggester]
     * Register argument parser and suggester for given type and key.
     * Key is used to define namespace for given parser. It is used to identify which parsers with same type should be used.
     * Argument type is used to define what type is compatible with given parser.
     *
     * @param type type of argument
     * @param key key of argument
     * @param resolver parser and suggester for given type
     * @return this builder
     */
    <IN, T, ARGUMENT extends Argument<T>, RESOLVER extends TypedParser<SENDER, IN, T, ARGUMENT> & Suggester<SENDER, T>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> argument(Class<T> type, ArgumentKey key, RESOLVER resolver);

    <T>
    LiteCommandsBuilder<SENDER, SETTINGS, B> context(Class<T> on, ContextProvider<SENDER, T> bind);

    <T>
    LiteCommandsBuilder<SENDER, SETTINGS, B> bind(Class<T> on, BindProvider<T> bindProvider);

    <T>
    LiteCommandsBuilder<SENDER, SETTINGS, B> bind(Class<T> on, Supplier<T> bind);

    LiteCommandsBuilder<SENDER, SETTINGS, B> bindUnsafe(Class<?> on, Supplier<?> bind);

    LiteCommandsBuilder<SENDER, SETTINGS, B> scheduler(Scheduler scheduler);

    <T, CONTEXT>
    LiteCommandsBuilder<SENDER, SETTINGS, B> message(MessageKey<CONTEXT> key, Message<T, CONTEXT> message);

    <T, CONTEXT>
    LiteCommandsBuilder<SENDER, SETTINGS, B> message(MessageKey<CONTEXT> key, T message);

    LiteCommandsBuilder<SENDER, SETTINGS, B> editorGlobal(Editor<SENDER> editor);

    LiteCommandsBuilder<SENDER, SETTINGS, B> editor(Scope scope, Editor<SENDER> editor);

    LiteCommandsBuilder<SENDER, SETTINGS, B> validatorGlobal(Validator<SENDER> validator);

    LiteCommandsBuilder<SENDER, SETTINGS, B> validator(Scope scope, Validator<SENDER> validator);

    default LiteCommandsBuilder<SENDER, SETTINGS, B> validatorMarked(Validator<SENDER> validator) {
        return validator(ValidatorScope.of(validator.getClass()), validator);
    }

    <T>
    LiteCommandsBuilder<SENDER, SETTINGS, B> result(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler);

    LiteCommandsBuilder<SENDER, SETTINGS, B> resultUnexpected(ResultHandler<SENDER, Object> handler);

    <E extends Throwable>
    LiteCommandsBuilder<SENDER, SETTINGS, B> exception(Class<E> exceptionType, ExceptionHandler<SENDER, ? extends E> handler);

    LiteCommandsBuilder<SENDER, SETTINGS, B> exceptionUnexpected(ExceptionHandler<SENDER, Throwable> handler);

    LiteCommandsBuilder<SENDER, SETTINGS, B> missingPermission(MissingPermissionsHandler<SENDER> handler);

    LiteCommandsBuilder<SENDER, SETTINGS, B> invalidUsage(InvalidUsageHandler<SENDER> handler);

    LiteCommandsBuilder<SENDER, SETTINGS, B> wrapper(Wrapper wrapper);

    LiteCommandsBuilder<SENDER, SETTINGS, B> schematicGenerator(SchematicGenerator<SENDER> schematicGenerator);

    LiteCommandsBuilder<SENDER, SETTINGS, B> schematicGenerator(SchematicFormat format);

    LiteCommandsBuilder<SENDER, SETTINGS, B> selfProcessor(LiteBuilderProcessor<SENDER, SETTINGS> processor);

    LiteCommandsBuilder<SENDER, SETTINGS, B> preProcessor(LiteBuilderProcessor<SENDER, SETTINGS> preProcessor);

    LiteCommandsBuilder<SENDER, SETTINGS, B> postProcessor(LiteBuilderProcessor<SENDER, SETTINGS> postProcessor);

    LiteCommandsBuilder<SENDER, SETTINGS, B> extension(LiteExtension<SENDER> extension);

    <E extends LiteExtension<SENDER>>
    LiteCommandsBuilder<SENDER, SETTINGS, B> extension(E extension, UnaryOperator<E> configuration);

    LiteCommands<SENDER> build();

    LiteCommands<SENDER> build(boolean register);

}
