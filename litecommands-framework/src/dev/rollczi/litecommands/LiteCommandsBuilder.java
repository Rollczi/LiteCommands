package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import dev.rollczi.litecommands.bind.BindChainedProvider;
import dev.rollczi.litecommands.bind.BindProvider;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.context.ContextChainedProvider;
import dev.rollczi.litecommands.event.Subscriber;
import dev.rollczi.litecommands.extension.annotations.AnnotationsExtension;
import dev.rollczi.litecommands.permission.PermissionStrictHandler;
import dev.rollczi.litecommands.processor.LiteBuilderAction;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.extension.LiteExtension;
import dev.rollczi.litecommands.editor.Editor;
import dev.rollczi.litecommands.handler.exception.ExceptionHandler;
import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.message.InvokedMessage;
import dev.rollczi.litecommands.message.Message;
import dev.rollczi.litecommands.message.MessageKey;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSettingsConfigurator;
import dev.rollczi.litecommands.scheduler.Scheduler;
import dev.rollczi.litecommands.schematic.SchematicFastFormat;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicGenerator;
import dev.rollczi.litecommands.scope.Scope;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.strict.StrictMode;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorScope;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

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
     * Returns advanced builder. This builder is used to configure advanced settings.
     */
    @ApiStatus.Experimental
    <A extends LiteCommandsAdvanced<SENDER, SETTINGS, A>>
    A advanced();

    /**
     * Configure platform settings.
     *
     * @see PlatformSettings
     * @param configurator configurator for platform settings
     * @return this builder
     */
    B settings(PlatformSettingsConfigurator<SETTINGS> configurator);

    /**
     * Register commands from given provider.
     *
     * @see LiteCommandsBuilder#commands(Object...)
     * @see LiteCommandsProvider
     * @param commandsProvider provider of commands
     * @return this builder
     */
    B commands(LiteCommandsProvider<SENDER> commandsProvider);

    /**
     * This method is used to register additional commands.
     * There are several types of objects that can be registered:
     * <b>Using annotations:</b>
     * <ul>
     *     <li>An instance annotated with {@link dev.rollczi.litecommands.annotations.command.Command}</li>
     *     <li>An instance annotated with {@link dev.rollczi.litecommands.annotations.command.RootCommand}</li>
     *     <li>A class annotated with {@link dev.rollczi.litecommands.annotations.command.Command}</li>
     *     <li>A class annotated with {@link dev.rollczi.litecommands.annotations.command.RootCommand}</li>
     * </ul>
     * <b>Programmatically:</b>
     * <ul>
     *     <li>An instance of {@link dev.rollczi.litecommands.programmatic.LiteCommand}</li>
     * </ul>
     * Please note that this method is experimental and may be deprecated or removed in the future.
     *
     * @see LiteCommandsBuilder#commands(LiteCommandsProvider)
     * @param commands commands to register
     * @return This method returns the current LiteCommand builder instance
     * This allows you to chain multiple calls together, using the builder design pattern.
     */
    B commands(Object... commands);

    <T>
    B argumentParser(Class<T> type, Parser<SENDER, T> parser);

    <T>
    B argumentParser(Class<T> type, ArgumentKey key, Parser<SENDER, T> parser);

    <T>
    B argumentSuggestion(Class<T> type, SuggestionResult suggestion);

    <T>
    B argumentSuggestion(Class<T> type, ArgumentKey key, SuggestionResult suggestion);

    <T>
    B argumentSuggester(Class<T> type, Suggester<SENDER, T> suggester);

    <T>
    B argumentSuggester(Class<T> type, ArgumentKey key, Suggester<SENDER, T> suggester);

    /**
     * [Argument Parser and Suggester]
     * Register argument parser and suggester for given type.
     *
     * @param type type of argument
     * @param resolver parser and suggester for a given type
     * @return this builder
     */
    <T>
    B argument(Class<T> type, ArgumentResolverBase<SENDER, T> resolver);

    /**
     * [Keyed Argument Parser and Suggester]
     * Register argument parser and suggester for a given type and key.
     * Key is used to define namespace for given parser. It is used to identify which parsers with same type should be used.
     * Argument type is used to define what type is compatible with given parser.
     *
     * @param type     type of argument
     * @param key      key of argument
     * @param resolver parser and suggester for a given type
     * @return this builder
     */
    <T>
    B argument(Class<T> type, ArgumentKey key, ArgumentResolverBase<SENDER, T> resolver);

    default <T> B context(Class<T> on, ContextProvider<SENDER, T> bind) {
        this.advanced().context(on, (ContextChainedProvider<SENDER, T>) bind);
        return (B) this;
    }

    default <T> B bind(Class<T> on, BindProvider<T> bindProvider) {
        this.advanced().bind(on, (BindChainedProvider<T>) bindProvider);
        return (B) this;
    }

    <T>
    B bind(Class<T> on, Supplier<T> bind);

    B bindUnsafe(Class<?> on, Supplier<?> bind);

    B scheduler(Scheduler scheduler);

    <T, CONTEXT>
    B message(MessageKey<CONTEXT> key, Message<T, CONTEXT> message);

    <T, CONTEXT>
    B message(MessageKey<CONTEXT> key, InvokedMessage<SENDER, T, CONTEXT> message);

    <T, CONTEXT>
    B message(MessageKey<CONTEXT> key, T message);

    B editorGlobal(Editor<SENDER> editor);

    B editor(Scope scope, Editor<SENDER> editor);

    B validatorGlobal(Validator<SENDER> validator);

    B validator(Scope scope, Validator<SENDER> validator);

    default B validatorMarked(Validator<SENDER> validator) {
        return validator(ValidatorScope.of(validator.getClass()), validator);
    }

    <T>
    B result(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler);

    B resultUnexpected(ResultHandler<SENDER, Object> handler);

    <E extends Throwable>
    B exception(Class<E> exceptionType, ExceptionHandler<SENDER, ? extends E> handler);

    B exceptionUnexpected(ExceptionHandler<SENDER, Throwable> handler);

    B missingPermission(MissingPermissionsHandler<SENDER> handler);

    B invalidUsage(InvalidUsageHandler<SENDER> handler);

    B schematicGenerator(SchematicGenerator<SENDER> schematicGenerator);

    B schematicGenerator(SchematicFormat format);

    B schematicGenerator(SchematicFastFormat format);

    /**
     * Set the default strict mode for all commands.
     * If strict mode is enabled, the command will fail if the user provides too many arguments.
     */
    @ApiStatus.Experimental
    B strictMode(StrictMode strictMode);

    /**
     * Set the default permission strict mode for all commands.
     */
    @ApiStatus.Experimental
    B permissionStrict(PermissionStrictHandler permissionStrictHandler);

    /**
     * Register event listener for the LiteCommands event system.
     * See {@link Event}, {@link Subscriber} and {@link EventListener} for more information.
     * Example listener:&#64;
     * <pre>
     * {@code
     *     public class MyListener implements EventListener {
     *
     *         \@Subscriber
     *         public void onEvent(CommandPreExecutionEvent event) {
     *             // your code
     *         }
     *
     *         \@Subscriber
     *         public void onEvent(CommandPostExecutionEvent event) {
     *             // your code
     *         }
     *     }
     * }
     * </pre>
     */
    @ApiStatus.Experimental
    B listener(EventListener listener);

    /**
     * @deprecated use {@link LiteCommandsBuilder#self(LiteBuilderAction)} instead
     */
    @Deprecated
    default B selfProcessor(LiteBuilderAction<SENDER, SETTINGS> processor) {
        return self(processor);
    }

    /**
     * @deprecated use {@link LiteCommandsBuilder#beforeBuild(LiteBuilderAction)} instead
     */
    @Deprecated
    default B preProcessor(LiteBuilderAction<SENDER, SETTINGS> preProcessor) {
        return beforeBuild(preProcessor);
    }

    /**
     * @deprecated use {@link LiteCommandsBuilder#afterBuild(LiteBuilderAction)} instead
     */
    @Deprecated
    default B postProcessor(LiteBuilderAction<SENDER, SETTINGS> postProcessor) {
        return afterBuild(postProcessor);
    }

    B self(LiteBuilderAction<SENDER, SETTINGS> action);

    B beforeBuild(LiteBuilderAction<SENDER, SETTINGS> action);

    B afterBuild(LiteBuilderAction<SENDER, SETTINGS> action);

    /**
     * Register extension for this builder.
     * @param extension extension to register
     * <b>Example:</b>
     * <pre>
     *  {@code
     *  .extension(new LiteAdventureExtension<>())
     *  }
     * </pre>
     * @return this builder
     * @param <CONFIGURATION> type of configuration
     */
    <CONFIGURATION>
    B extension(LiteExtension<SENDER, CONFIGURATION> extension);

    /**
     * Register extension for this builder with configurator.
     *
     * @param extension extension to register
     * @param configurator configurator for extension
     * <b>Example:</b>
     * <pre>
     *  {@code
     *  .extension(new LiteAdventureExtension<>(), configuration -> configuration
     *    .colorizeArgument(true)
     *    .miniMessage(true)
     *    .legacyColor(true)
     *    .serializer(ComponentSerializer.miniMessage())
     *  )
     *  }
     * </pre>
     *
     * @return this builder
     * @param <CONFIGURATION> type of configuration
     */
    <CONFIGURATION, E extends LiteExtension<SENDER, CONFIGURATION>>
    B extension(E extension, LiteConfigurator<CONFIGURATION> configurator);

    /**
     * [Experimental]
     * Register extension for annotations processing/
     * Please note that this method is experimental and may be deprecated or removed in the future.
     * @param configurator configurator for extension
     * <ul>
     *  <li>processor - register new annotation processor</li>
     *  <li>validator - register new validator for requirement</li>
     * </ul>
     * <b>Example:</b>
     * <pre>
     * {@code
     * .extensionAnnotation(extension -> extension
     *     .processor(new MyAnnotationProcessor())
     *     .validator(String.class, IsValid.class, new StringValidator())
     * )
     * }
     * </pre>
     * @return this builder
     */
    @ApiStatus.Experimental
    B annotations(LiteConfigurator<AnnotationsExtension<SENDER>> configurator);

    LiteCommands<SENDER> build();

    LiteCommands<SENDER> build(boolean register);

}
