package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.bind.Bind;
import dev.rollczi.litecommands.modern.bind.BindContextual;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultMapper;
import dev.rollczi.litecommands.modern.editor.CommandEditor;
import dev.rollczi.litecommands.modern.platform.Platform;
import dev.rollczi.litecommands.modern.validator.CommandValidator;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedFactory;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface LiteCommandsBuilder<SENDER, C extends LiteConfiguration, B extends LiteCommandsBuilder<SENDER, C, B>> {

    LiteCommandsBuilder<SENDER, C, B> configure(UnaryOperator<C> operator);

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

    LiteCommandsBuilder<SENDER, C, B> wrapperFactory(WrappedExpectedFactory factory);

    <T> LiteCommandsBuilder<SENDER, C, B> resultHandler(Class<T> resultType, CommandExecuteResultHandler<SENDER, T> handler);

    <T> LiteCommandsBuilder<SENDER, C, B> resultMapper(Class<T> mapperFromType, CommandExecuteResultMapper<SENDER, T, ?> mapper);

    <E extends LiteCommandsExtension<SENDER>> LiteCommandsBuilder<SENDER, C, B> extension(E extension);

    <E extends LiteCommandsExtension<SENDER>> LiteCommandsBuilder<SENDER, C, B> extension(E extension, UnaryOperator<E> configuration);

    LiteCommandsBuilder<SENDER, C, B> platform(Platform<SENDER, C> platform);

    LiteCommands<SENDER> register();

}
