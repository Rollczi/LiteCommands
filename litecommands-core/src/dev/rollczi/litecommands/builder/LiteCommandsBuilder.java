package dev.rollczi.litecommands.builder;

import dev.rollczi.litecommands.argument.parser.ArgumentParser;
import dev.rollczi.litecommands.bind.Bind;
import dev.rollczi.litecommands.bind.BindContextual;
import dev.rollczi.litecommands.builder.extension.LiteCommandsExtension;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPostProcessor;
import dev.rollczi.litecommands.builder.processor.LiteBuilderPreProcessor;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.command.CommandExecuteResultMapper;
import dev.rollczi.litecommands.editor.CommandEditor;
import dev.rollczi.litecommands.platform.LiteSettings;
import dev.rollczi.litecommands.validator.Validator;
import dev.rollczi.litecommands.validator.ValidatorScope;
import dev.rollczi.litecommands.wrapper.Wrapper;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface LiteCommandsBuilder<SENDER, C extends LiteSettings, B extends LiteCommandsBuilder<SENDER, C, B>> {

    @Deprecated
    LiteCommandsBuilder<SENDER, C, B> applySettings(UnaryOperator<C> operator);

    @Deprecated
    LiteCommandsBuilder<SENDER, C, B> withEditor(String command, CommandEditor<SENDER> commandEditor);

    @Deprecated
    LiteCommandsBuilder<SENDER, C, B> withGlobalEditor(CommandEditor<SENDER> commandEditor);

    @Deprecated
    LiteCommandsBuilder<SENDER, C, B> withValidator(Validator<SENDER> validator);

    @Deprecated
    LiteCommandsBuilder<SENDER, C, B> withValidator(Validator<SENDER> validator, ValidatorScope scope);

    @Deprecated
    <INPUT, PARSED, PARSER extends ArgumentParser<SENDER, INPUT, PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, PARSER parser);

    @Deprecated
    <INPUT, PARSED, PARSER extends ArgumentParser<SENDER, INPUT, PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, String key, PARSER parser);

    @Deprecated
    <INPUT, PARSED, ARGUMENT extends Argument<PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, Class<ARGUMENT> argumentType, ArgumentParser<SENDER, INPUT, PARSED> resolver);

    @Deprecated
    <INPUT, PARSED, ARGUMENT extends Argument<PARSED>> LiteCommandsBuilder<SENDER, C, B> argumentParser(Class<PARSED> type, Class<ARGUMENT> argumentType, String key, ArgumentParser<SENDER, INPUT, PARSED> resolver);

    @Deprecated
    <T> LiteCommandsBuilder<SENDER, C, B> bindStatic(Class<T> on, Bind<T> bind);

    @Deprecated
    <T> LiteCommandsBuilder<SENDER, C, B> bindStatic(Class<T> on, Supplier<T> bind);

    @Deprecated
    LiteCommandsBuilder<SENDER, C, B> bindStaticUnsafe(Class<?> on, Supplier<?> bind);

    @Deprecated
    <T> LiteCommandsBuilder<SENDER, C, B> bindContext(Class<T> on, BindContextual<SENDER, T> bind);

    @Deprecated
    LiteCommandsBuilder<SENDER, C, B> registerWrapperFactory(Wrapper factory);

    @Deprecated
    <T> LiteCommandsBuilder<SENDER, C, B> resultHandler(Class<T> resultType, CommandExecuteResultHandler<SENDER, ? extends T> handler);

    @Deprecated
    <T> LiteCommandsBuilder<SENDER, C, B> resultMapper(Class<T> mapperFromType, CommandExecuteResultMapper<SENDER, T, ?> mapper);

    @Deprecated
    <E extends LiteCommandsExtension<SENDER>> LiteCommandsBuilder<SENDER, C, B> withExtension(E extension);

    @Deprecated
    <E extends LiteCommandsExtension<SENDER>> LiteCommandsBuilder<SENDER, C, B> withExtension(E extension, UnaryOperator<E> configuration);

    LiteCommandsBuilder<SENDER, C, B> preProcessor(LiteBuilderPreProcessor<SENDER, C> preProcessor);

    LiteCommandsBuilder<SENDER, C, B> postProcessor(LiteBuilderPostProcessor<SENDER, C> postProcessor);

    @Deprecated
    LiteCommands<SENDER> register();

}
