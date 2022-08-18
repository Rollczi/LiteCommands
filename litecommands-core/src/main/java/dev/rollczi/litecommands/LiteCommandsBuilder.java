package dev.rollczi.litecommands;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.factory.CommandEditor;
import dev.rollczi.litecommands.factory.CommandStateFactory;
import dev.rollczi.litecommands.handle.Handler;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.handle.PermissionHandler;
import dev.rollczi.litecommands.handle.Redirector;
import dev.rollczi.litecommands.injector.bind.AnnotationBind;
import dev.rollczi.litecommands.injector.bind.TypeBind;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.schematic.SchematicGenerator;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface LiteCommandsBuilder<SENDER> {

    LiteCommandsBuilder<SENDER> platform(RegistryPlatform<SENDER> registryPlatform);

    LiteCommandsBuilder<SENDER> commandFactory(CommandStateFactory<SENDER> factory);

    LiteCommandsBuilder<SENDER> configureFactory(Consumer<CommandStateFactory<SENDER>> consumer);

    LiteCommandsBuilder<SENDER> commandEditor(Class<?> commandClass, CommandEditor commandEditor);

    LiteCommandsBuilder<SENDER> commandEditor(String name, CommandEditor commandEditor);

    LiteCommandsBuilder<SENDER> commandGlobalEditor(CommandEditor allCommandsEditor);

    LiteCommandsBuilder<SENDER> schematicGenerator(SchematicGenerator schematicGenerator);

    LiteCommandsBuilder<SENDER> schematicFormat(SchematicFormat schematicFormat);

    <T> LiteCommandsBuilder<SENDER> resultHandler(Class<T> on, Handler<SENDER, T> handler);

    <FROM, TO> LiteCommandsBuilder<SENDER> redirectResult(Class<FROM> from, Class<TO> to, Redirector<FROM, TO> redirector);

    LiteCommandsBuilder<SENDER> invalidUsageHandler(InvalidUsageHandler<SENDER> handler);

    LiteCommandsBuilder<SENDER> permissionHandler(PermissionHandler<SENDER> handler);

    LiteCommandsBuilder<SENDER> command(Class<?>... commandClass);

    LiteCommandsBuilder<SENDER> commandInstance(Object... commandInstance);

    <T> LiteCommandsBuilder<SENDER> typeBind(Class<T> type, Supplier<T> supplier);

    <T> LiteCommandsBuilder<SENDER> typeBind(Class<T> type, TypeBind<T> typeBind);

    LiteCommandsBuilder<SENDER> typeUnsafeBind(Class<?> type, TypeBind<?> typeBind);

    <T, A extends Annotation> LiteCommandsBuilder<SENDER> annotatedBind(Class<T> type, Class<A> annotationType, AnnotationBind<T, SENDER, A> annotationBind);

    <T> LiteCommandsBuilder<SENDER> contextualBind(Class<T> on, Contextual<SENDER, T> contextual);

    <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, OneArgument<T> argument);

    <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, String by, OneArgument<T> argument);

    <T> LiteCommandsBuilder<SENDER> argumentMultilevel(Class<T> on, MultilevelArgument<T> argument);

    <T> LiteCommandsBuilder<SENDER> argumentMultilevel(Class<T> on, String by, MultilevelArgument<T> argument);

    <A extends Annotation> LiteCommandsBuilder<SENDER> argument(Class<A> annotation, Class<?> on, Argument<SENDER, A> argument);

    <A extends Annotation> LiteCommandsBuilder<SENDER> argument(Class<A> annotation, Class<?> on, String by, Argument<SENDER, A> argument);

    LiteCommandsBuilder<SENDER> executorFactory(CommandStateFactory<SENDER> commandStateFactory);

    Class<SENDER> getSenderType();

    LiteCommandsBuilder<SENDER> beforeRegister(LiteCommandsPreProcess<SENDER> preProcess);

    LiteCommandsBuilder<SENDER> afterRegister(LiteCommandsPostProcess<SENDER> postProcess);

    LiteCommands<SENDER> register();

}
