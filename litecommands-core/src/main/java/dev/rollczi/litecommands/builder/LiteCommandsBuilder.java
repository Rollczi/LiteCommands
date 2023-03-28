package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentParser;
import dev.rollczi.litecommands.bind.Bind;
import dev.rollczi.litecommands.bind.BindContextual;
import dev.rollczi.litecommands.builder.extension.LiteCommandsExtension;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPostProcessor;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPreProcessor;
import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.command.CommandExecuteResultMapper;
import dev.rollczi.litecommands.editor.CommandEditor;
import dev.rollczi.litecommands.platform.LiteSettings;
import dev.rollczi.litecommands.validator.CommandValidator;
import dev.rollczi.litecommands.wrapper.WrappedExpectedFactory;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface LiteCommandsBuilder<SENDER, C extends LiteSettings, B extends LiteCommandsBuilder<SENDER, C, B>> {

    LiteCommandsBuilder<SENDER, C, B> settings(UnaryOperator<C> operator);

    LiteCommandsBuilder<SENDER, C, B> editor(String command, CommandEditor<SENDER> commandEditor);

    LiteCommandsBuilder<SENDER, C, B> globalEditor(CommandEditor<SENDER> commandEditor);

    LiteCommandsBuilder<SENDER, C, B> validator(CommandValidator<SENDER> validator);

    <T, RESOLVER extends ArgumentParser<SENDER, T, Argument<T>>> LiteCommandsBuilder<SENDER, C, B> argument(Class<T> type, RESOLVER resolver);

    <T, RESOLVER extends ArgumentParser<SENDER, T, Argument<T>>> LiteCommandsBuilder<SENDER, C, B> argument(Class<T> type, String key, RESOLVER resolver);

    <T, ARGUMENT extends Argument<T>> LiteCommandsBuilder<SENDER, C, B> argument(Class<T> type, Class<ARGUMENT> argumentType, ArgumentParser<SENDER, T, ? extends ARGUMENT> resolver);

    <T, ARGUMENT extends Argument<T>> LiteCommandsBuilder<SENDER, C, B> argument(Class<T> type, Class<ARGUMENT> argumentType, String key, ArgumentParser<SENDER, T, ? extends ARGUMENT> resolver);

    <T> LiteCommandsBuilder<SENDER, C, B> typeBind(Class<T> on, Bind<T> bind);

    <T> LiteCommandsBuilder<SENDER, C, B> typeBind(Class<T> on, Supplier<T> bind);

    LiteCommandsBuilder<SENDER, C, B> typeBindUnsafe(Class<?> on, Supplier<?> bind);

    <T> LiteCommandsBuilder<SENDER, C, B> contextualBind(Class<T> on, BindContextual<SENDER, T> bind);

    LiteCommandsBuilder<SENDER, C, B> registerWrapperFactory(WrappedExpectedFactory factory);

    <T> LiteCommandsBuilder<SENDER, C, B> resultHandler(Class<T> resultType, CommandExecuteResultHandler<SENDER, ? extends T> handler);

    <T> LiteCommandsBuilder<SENDER, C, B> resultMapper(Class<T> mapperFromType, CommandExecuteResultMapper<SENDER, T, ?> mapper);

    <E extends LiteCommandsExtension<SENDER, C>> LiteCommandsBuilder<SENDER, C, B> extension(E extension);

    <E extends LiteCommandsExtension<SENDER, C>> LiteCommandsBuilder<SENDER, C, B> extension(E extension, UnaryOperator<E> configuration);

    LiteCommandsBuilder<SENDER, C, B> preProcessor(LiteBuilderPreProcessor<SENDER, C> preProcessor);

    LiteCommandsBuilder<SENDER, C, B> postProcessor(LiteBuilderPostProcessor<SENDER, C> postProcessor);

    LiteCommands<SENDER> register();

}
