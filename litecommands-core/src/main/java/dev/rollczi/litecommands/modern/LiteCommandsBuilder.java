package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.argument.ArgumentResolver;
import dev.rollczi.litecommands.modern.bind.Bind;
import dev.rollczi.litecommands.modern.bind.BindContextual;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultMapper;
import dev.rollczi.litecommands.modern.command.editor.CommandEditor;
import dev.rollczi.litecommands.modern.command.filter.CommandFilter;
import dev.rollczi.litecommands.modern.contextual.warpped.WrappedExpectedContextualFactory;
import dev.rollczi.litecommands.modern.platform.Platform;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResolver;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface LiteCommandsBuilder<SENDER, B extends LiteCommandsBuilder<SENDER, B>> {

    LiteCommandsBuilder<SENDER, B> editor(String command, CommandEditor<SENDER> commandEditor);

    LiteCommandsBuilder<SENDER, B> globalEditor(CommandEditor<SENDER> commandEditor);

    LiteCommandsBuilder<SENDER, B> filter(CommandFilter<SENDER> filter);

    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsBuilder<SENDER, B> argumentOnly(
        Class<T> type,
        ARG argument
    );

    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsBuilder<SENDER, B> argumentOnly(
        Class<T> type,
        ARG argument,
        ArgumentKey argumentKey
    );

    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>> & SuggestionResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsBuilder<SENDER, B> argument(
        Class<T> type,
        ARG argument
    );

    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>> & SuggestionResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsBuilder<SENDER, B> argument(
        Class<T> type,
        ARG argument,
        ArgumentKey argumentKey
    );

    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT>> LiteCommandsBuilder<SENDER, B> argumentOnly(
        Class<D> determinantType,
        Class<T> expectedType,
        Class<ARGUMENT> contextType,
        ARG argument
    );

    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT>> LiteCommandsBuilder<SENDER, B> argumentOnly(
        Class<D> determinantType,
        Class<T> expectedType,
        Class<ARGUMENT> contextType,
        ARG argument,
        ArgumentKey argumentKey
    );

    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT> & SuggestionResolver<SENDER, D, T, ARGUMENT>> LiteCommandsBuilder<SENDER, B> argument(Class<D> determinantType, Class<T> expectedType, Class<ARGUMENT> contextType, ARG argument);

    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT> & SuggestionResolver<SENDER, D, T, ARGUMENT>> LiteCommandsBuilder<SENDER, B> argument(Class<D> determinantType, Class<T> expectedType, Class<ARGUMENT> contextType, ARG argument, ArgumentKey argumentKey);

    <T> LiteCommandsBuilder<SENDER, B> typeBind(Class<T> on, Bind<T> bind);

    <T> LiteCommandsBuilder<SENDER, B> typeBind(Class<T> on, Supplier<T> bind);

    LiteCommandsBuilder<SENDER, B> typeBindUnsafe(Class<?> on, Supplier<?> bind);

    <T> LiteCommandsBuilder<SENDER, B> contextualBind(Class<T> on, BindContextual<SENDER, T> bind);

    LiteCommandsBuilder<SENDER, B> wrappedExpectedContextualFactory(WrappedExpectedContextualFactory factory);

    <T> LiteCommandsBuilder<SENDER, B> resultHandler(Class<T> resultType, CommandExecuteResultHandler<SENDER, T> handler);

    <T> LiteCommandsBuilder<SENDER, B> resultMapper(Class<T> mapperFromType, CommandExecuteResultMapper<SENDER, T, ?> mapper);

        <E extends LiteExtension<SENDER>> LiteCommandsBuilder<SENDER, B> withExtension(E extension);

    <E extends LiteExtension<SENDER>> LiteCommandsBuilder<SENDER, B> withExtension(E extension, UnaryOperator<E> configuration);

    LiteCommandsBuilder<SENDER, B> platform(Platform<SENDER> platform);

    LiteCommands<SENDER> register();

}
