package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommands;
import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteExtension;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationResolver;
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

import java.lang.annotation.Annotation;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface LiteCommandsAnnotationBuilder<SENDER, B extends LiteCommandsAnnotationBuilder<SENDER, B>> extends LiteCommandsBuilder<SENDER, B> {

    LiteCommandsAnnotationBuilder<SENDER, B> command(Object... commands);

    LiteCommandsAnnotationBuilder<SENDER, B> command(Class<?>... commands);

    <A extends Annotation> B annotation(Class<A> annotation, CommandAnnotationResolver<SENDER, A> resolver);

    <A extends Annotation> B annotation(Class<A> annotation, UnaryOperator<CommandAnnotationResolver<SENDER, A>> resolver);

    <A extends Annotation> B annotationMethod(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver);

    <A extends Annotation> B annotationMethod(Class<A> annotation, UnaryOperator<CommandAnnotationMethodResolver<SENDER, A>> resolver);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> editor(String command, CommandEditor<SENDER> commandEditor);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> globalEditor(CommandEditor<SENDER> commandEditor);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> filter(CommandFilter<SENDER> filter);

    @Override
    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsAnnotationBuilder<SENDER, B> argumentOnly(Class<T> type, ARG argument);

    @Override
    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsAnnotationBuilder<SENDER, B> argumentOnly(Class<T> type, ARG argument, ArgumentKey argumentKey);

    @Override
    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>> & SuggestionResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsAnnotationBuilder<SENDER, B> argument(Class<T> type, ARG argument);

    @Override
    <T, ARG extends ArgumentResolver<SENDER, Object, T, Argument<Object, T>> & SuggestionResolver<SENDER, Object, T, Argument<Object, T>>> LiteCommandsAnnotationBuilder<SENDER, B> argument(Class<T> type, ARG argument, ArgumentKey argumentKey);

    @Override
    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT>> LiteCommandsAnnotationBuilder<SENDER, B> argumentOnly(Class<D> determinantType, Class<T> expectedType, Class<ARGUMENT> contextType, ARG argument);

    @Override
    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT>> LiteCommandsAnnotationBuilder<SENDER, B> argumentOnly(Class<D> determinantType, Class<T> expectedType, Class<ARGUMENT> contextType, ARG argument, ArgumentKey argumentKey);

    @Override
    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT> & SuggestionResolver<SENDER, D, T, ARGUMENT>> LiteCommandsAnnotationBuilder<SENDER, B> argument(Class<D> determinantType, Class<T> expectedType, Class<ARGUMENT> contextType, ARG argument);

    @Override
    <D, T, ARGUMENT extends Argument<D, T>, ARG extends ArgumentResolver<SENDER, D, T, ARGUMENT> & SuggestionResolver<SENDER, D, T, ARGUMENT>> LiteCommandsAnnotationBuilder<SENDER, B> argument(Class<D> determinantType, Class<T> expectedType, Class<ARGUMENT> contextType, ARG argument, ArgumentKey argumentKey);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> typeBind(Class<T> on, Bind<T> bind);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> typeBind(Class<T> on, Supplier<T> bind);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> typeBindUnsafe(Class<?> on, Supplier<?> bind);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> contextualBind(Class<T> on, BindContextual<SENDER, T> bind);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> wrappedExpectedContextualFactory(WrappedExpectedContextualFactory factory);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> resultHandler(Class<T> resultType, CommandExecuteResultHandler<SENDER, T> handler);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> resultMapper(Class<T> mapperFromType, CommandExecuteResultMapper<SENDER, T, ?> mapper);

    @Override
    <E extends LiteExtension<SENDER>> LiteCommandsAnnotationBuilder<SENDER, B> withExtension(E extension);

    @Override
    <E extends LiteExtension<SENDER>> LiteCommandsAnnotationBuilder<SENDER, B> withExtension(E extension, UnaryOperator<E> configuration);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> platform(Platform<SENDER> platform);

    @Override
    LiteCommands<SENDER> register();

    static <SENDER, B extends LiteCommandsAnnotationBuilderImpl<SENDER, B>> LiteCommandsAnnotationBuilder<SENDER, B> create(Class<SENDER> senderClass) {
        return new LiteCommandsAnnotationBuilderImpl<>(senderClass);
    }

}
