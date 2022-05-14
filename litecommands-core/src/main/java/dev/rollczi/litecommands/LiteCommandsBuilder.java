package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.factory.CommandEditor;
import dev.rollczi.litecommands.factory.CommandStateFactory;
import dev.rollczi.litecommands.handle.Handler;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import dev.rollczi.litecommands.scheme.Scheme;
import dev.rollczi.litecommands.scheme.SchemeFormat;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface LiteCommandsBuilder<SENDER> {

    LiteCommandsBuilder<SENDER> platform(RegistryPlatform<SENDER> registryPlatform);

    LiteCommandsBuilder<SENDER> commandFactory(CommandStateFactory factory);

    LiteCommandsBuilder<SENDER> configureFactory(Consumer<CommandStateFactory> consumer);

    LiteCommandsBuilder<SENDER> commandEditor(Class<?> commandClass, CommandEditor commandEditor);

    LiteCommandsBuilder<SENDER> schemeFormat(SchemeFormat schemeFormat);

    <T> LiteCommandsBuilder<SENDER> resultHandler(Class<T> on, Handler<SENDER, T> handler);

    LiteCommandsBuilder<SENDER> invalidUsageHandler(InvalidUsageHandler<SENDER> handler);

    LiteCommandsBuilder<SENDER> command(Class<?>... commandClass);

    LiteCommandsBuilder<SENDER> commandInstance(Object... commandInstance);

    LiteCommandsBuilder<SENDER> typeBind(Class<?> type, Supplier<?> supplier);

    <T> LiteCommandsBuilder<SENDER> contextualBind(Class<T> on, Contextual<SENDER, T> contextual);

    <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, OneArgument<T> argument);

    <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, String by, OneArgument<T> argument);

    <T> LiteCommandsBuilder<SENDER> argumentMultilevel(Class<T> on, MultilevelArgument<T> argument);

    <T> LiteCommandsBuilder<SENDER> argumentMultilevel(Class<T> on, String by, MultilevelArgument<T> argument);

    <A extends Annotation> LiteCommandsBuilder<SENDER> argument(Class<A> annotation, Class<?> on, Argument<A> argument);

    <A extends Annotation> LiteCommandsBuilder<SENDER> argument(Class<A> annotation, Class<?> on, String by, Argument<A> argument);

    LiteCommandsBuilder<SENDER> executorFactory(CommandStateFactory commandStateFactory);

    LiteCommands<SENDER> register();

}
