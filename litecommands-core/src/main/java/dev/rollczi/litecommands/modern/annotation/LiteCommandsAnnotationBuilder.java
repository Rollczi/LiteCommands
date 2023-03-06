package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommands;
import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteExtension;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentParser;
import dev.rollczi.litecommands.modern.bind.Bind;
import dev.rollczi.litecommands.modern.bind.BindContextual;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultMapper;
import dev.rollczi.litecommands.modern.editor.CommandEditor;
import dev.rollczi.litecommands.modern.validator.CommandValidator;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedFactory;
import dev.rollczi.litecommands.modern.platform.Platform;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface LiteCommandsAnnotationBuilder<SENDER, B extends LiteCommandsAnnotationBuilder<SENDER, B>> extends LiteCommandsBuilder<SENDER, B> {

    LiteCommandsAnnotationBuilder<SENDER, B> command(Object... commands);

    LiteCommandsAnnotationBuilder<SENDER, B> command(Class<?>... commands);

    <A extends Annotation> B annotation(Class<A> annotation, CommandAnnotationClassResolver<SENDER, A> resolver);

    <A extends Annotation> B annotation(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver);

    <A extends Annotation> B annotation(Class<A> annotation, CommandAnnotationMetaApplicator<SENDER, A> resolver);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> editor(String command, CommandEditor<SENDER> commandEditor);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> globalEditor(CommandEditor<SENDER> commandEditor);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> validator(CommandValidator<SENDER> validator);

    @Override
    <T, RESOLVER extends ArgumentParser<SENDER, T, Argument<T>>> LiteCommandsBuilder<SENDER, B> argument(Class<T> type, RESOLVER resolver);

    @Override
    <T, RESOLVER extends ArgumentParser<SENDER, T, Argument<T>>> LiteCommandsBuilder<SENDER, B> argument(Class<T> type, String key, RESOLVER resolver);

    @Override
    <T, ARGUMENT extends Argument<T>> LiteCommandsBuilder<SENDER, B> argument(Class<T> type, Class<ARGUMENT> argumentType, ArgumentParser<SENDER, T, ? extends ARGUMENT> resolver);

    @Override
    <T, ARGUMENT extends Argument<T>> LiteCommandsBuilder<SENDER, B> argument(Class<T> type, Class<ARGUMENT> argumentType, String key, ArgumentParser<SENDER, T, ? extends ARGUMENT> resolver);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> typeBind(Class<T> on, Bind<T> bind);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> typeBind(Class<T> on, Supplier<T> bind);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> typeBindUnsafe(Class<?> on, Supplier<?> bind);

    @Override
    <T> LiteCommandsAnnotationBuilder<SENDER, B> contextualBind(Class<T> on, BindContextual<SENDER, T> bind);

    @Override
    LiteCommandsAnnotationBuilder<SENDER, B> wrapperFactory(WrappedExpectedFactory factory);

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
