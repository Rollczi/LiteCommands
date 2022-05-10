package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.one.OneArgument;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.factory.CommandEditor;
import dev.rollczi.litecommands.factory.CommandStateFactory;
import dev.rollczi.litecommands.handle.Handler;
import dev.rollczi.litecommands.platform.RegistryPlatform;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface LiteCommandsBuilder<SENDER> {

    LiteCommandsBuilder<SENDER> platform(RegistryPlatform<SENDER> registryPlatform);

    LiteCommandsBuilder<SENDER> commandFactory(CommandStateFactory factory);

    LiteCommandsBuilder<SENDER> configureFactory(Consumer<CommandStateFactory> consumer);

    LiteCommandsBuilder<SENDER> commandEditor(Class<?> commandClass, CommandEditor commandEditor);

    <T> LiteCommandsBuilder<SENDER> resultHandler(Class<T> on, Handler<SENDER, T> handler);

    LiteCommandsBuilder<SENDER> command(Class<?>... commandClass);

    LiteCommandsBuilder<SENDER> commandInstance(Object... commandInstance);

    LiteCommandsBuilder<SENDER> typeBind(Class<?> type, Supplier<?> supplier);

    <T> LiteCommandsBuilder<SENDER> contextualBind(Class<T> on, Contextual<SENDER, T> contextual);

    <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, OneArgument<T> oneArgument);

    <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, String by, OneArgument<T> argument);

    <A extends Annotation> LiteCommandsBuilder<SENDER> argument(Class<A> annotation, Class<?> on, Argument<A> argument);

    <A extends Annotation> LiteCommandsBuilder<SENDER> argument(Class<A> annotation, Class<?> on, String by, Argument<A> argument);

    LiteCommandsBuilder<SENDER> executorFactory(CommandStateFactory commandStateFactory);

    LiteCommands<SENDER> register();

}
