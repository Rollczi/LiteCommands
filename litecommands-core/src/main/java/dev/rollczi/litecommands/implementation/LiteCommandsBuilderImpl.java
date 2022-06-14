package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsProcess;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.simple.MultilevelArgument;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.argument.simple.SimpleMultilevelArgument;
import dev.rollczi.litecommands.argument.option.Opt;
import dev.rollczi.litecommands.argument.option.OptionArgument;
import dev.rollczi.litecommands.command.permission.LitePermissions;
import dev.rollczi.litecommands.contextual.Contextual;
import dev.rollczi.litecommands.factory.CommandEditor;
import dev.rollczi.litecommands.factory.CommandEditorRegistry;
import dev.rollczi.litecommands.factory.CommandStateFactory;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.CommandService;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.handle.Handler;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.handle.PermissionHandler;
import dev.rollczi.litecommands.handle.Redirector;
import dev.rollczi.litecommands.implementation.injector.InjectorProvider;
import dev.rollczi.litecommands.injector.bind.AnnotationBind;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.injector.InjectorSettings;
import dev.rollczi.litecommands.injector.bind.TypeBind;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import dev.rollczi.litecommands.scheme.Scheme;
import dev.rollczi.litecommands.scheme.SchemeFormat;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

class LiteCommandsBuilderImpl<SENDER> implements LiteCommandsBuilder<SENDER> {

    private final Class<SENDER> senderType;

    private final InjectorSettings<SENDER> injectorSettings = InjectorProvider.settings();
    private final ExecuteResultHandler<SENDER> executeResultHandler = new ExecuteResultHandler<>();

    private RegistryPlatform<SENDER> registryPlatform;
    private CommandStateFactory<SENDER> commandStateFactory;

    private LiteCommandsProcess<SENDER> preProcess = (builder, platform, injector) -> {};
    private LiteCommandsProcess<SENDER> postProcess = (builder, platform, injector) -> {};

    private final Set<Consumer<CommandStateFactory<SENDER>>> commandStateFactoryEditors = new HashSet<>();
    private final CommandEditorRegistry editorRegistry = new CommandEditorRegistry();

    private final Set<Class<?>> commandsClasses = new HashSet<>();
    private final Set<Object> commandsInstances = new HashSet<>();
    private final ArgumentsRegistry<SENDER> argumentsRegistry = new ArgumentsRegistry<>();

    private LiteCommandsBuilderImpl(Class<SENDER> senderType) {
        this.senderType = senderType;
    }

    @Override
    public LiteCommandsBuilderImpl<SENDER> platform(RegistryPlatform<SENDER> registryPlatform) {
        this.registryPlatform = registryPlatform;
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER> commandFactory(CommandStateFactory<SENDER> factory) {
        this.commandStateFactory = factory;
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER> configureFactory(Consumer<CommandStateFactory<SENDER>> consumer) {
        commandStateFactoryEditors.add(consumer);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER> commandEditor(Class<?> commandClass, CommandEditor commandEditor) {
        this.editorRegistry.registerEditor(commandClass, commandEditor);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER> commandEditor(String name, CommandEditor commandEditor) {
        this.editorRegistry.registerEditor(name, commandEditor);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER> commandGlobalEditor(CommandEditor commandsEditor) {
        this.editorRegistry.registerGlobalEditor(commandsEditor);
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER> schemeFormat(SchemeFormat schemeFormat) {
        this.executeResultHandler.schemeFormat(schemeFormat);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilderImpl<SENDER> resultHandler(Class<T> on, Handler<SENDER, T> handler) {
        this.executeResultHandler.register(on, handler);
        return this;
    }

    @Override
    public <FROM, TO> LiteCommandsBuilder<SENDER> redirectResult(Class<FROM> from, Class<TO> to, Redirector<FROM, TO> redirector) {
        this.executeResultHandler.redirector(from, to, redirector);
        return this;
    }

    public LiteCommandsBuilder<SENDER> invalidUsageHandler(InvalidUsageHandler<SENDER> handler) {
        return this.resultHandler(Scheme.class, handler);
    }

    @Override
    public LiteCommandsBuilder<SENDER> permissionHandler(PermissionHandler<SENDER> handler) {
        return this.resultHandler(LitePermissions.class, handler);
    }

    @Override
    public LiteCommandsBuilderImpl<SENDER> executorFactory(CommandStateFactory<SENDER> commandStateFactory) {
        this.commandStateFactory = commandStateFactory;
        return this;
    }

    @Override
    public LiteCommandsBuilderImpl<SENDER> command(Class<?>... commandClass) {
        commandsClasses.addAll(Arrays.asList(commandClass));
        return this;
    }

    @Override
    public LiteCommandsBuilderImpl<SENDER> commandInstance(Object... commandInstance) {
        commandsInstances.addAll(Arrays.asList(commandInstance));
        return this;
    }

    @Override
    public <T> LiteCommandsBuilderImpl<SENDER> typeBind(Class<T> type, Supplier<T> supplier) {
        this.injectorSettings.typeBind(type, supplier);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER> typeBind(Class<T> type, TypeBind<T> typeBind) {
        this.injectorSettings.typeBind(type, typeBind);
        return this;
    }

    @Override
    public <T, A extends Annotation> LiteCommandsBuilder<SENDER> annotatedBind(Class<T> type, Class<A> annotationType, AnnotationBind<T, A> annotationBind) {
        this.injectorSettings.annotationBind(type, annotationType, annotationBind);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilderImpl<SENDER> contextualBind(Class<T> on, Contextual<SENDER, T> contextual) {
        this.injectorSettings.contextualBind(on, contextual);
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, OneArgument<T> argument) {
        return this.argumentMultilevel(on, argument);
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER> argument(Class<T> on, String by, OneArgument<T> argument) {
        return this.argumentMultilevel(on, by, argument);
    }

    @Override
    public <T> LiteCommandsBuilderImpl<SENDER> argumentMultilevel(Class<T> on, MultilevelArgument<T> argument) {
        this.argument(Arg.class, on, new SimpleMultilevelArgument<>(argument));
        this.argument(Opt.class, on, new OptionArgument<>(on, argument));
        return this;
    }

    @Override
    public <T> LiteCommandsBuilder<SENDER> argumentMultilevel(Class<T> on, String by, MultilevelArgument<T> argument) {
        this.argument(Arg.class, on, by, new SimpleMultilevelArgument<>(argument));
        this.argument(Opt.class, on, by, new OptionArgument<>(on, argument));
        return this;
    }

    @Override
    public <A extends Annotation> LiteCommandsBuilderImpl<SENDER> argument(Class<A> annotation, Class<?> on, Argument<SENDER, A> argument) {
        this.argumentsRegistry.register(annotation, on, argument);
        return this;
    }

    @Override
    public <A extends Annotation> LiteCommandsBuilderImpl<SENDER> argument(Class<A> annotation, Class<?> on, String by, Argument<SENDER, A> argument) {
        this.argumentsRegistry.register(annotation, on, by, argument);
        return this;
    }

    @Override
    public Class<SENDER> getSenderType() {
        return this.senderType;
    }

    @Override
    public LiteCommandsBuilder<SENDER> beforeRegister(LiteCommandsProcess<SENDER> preProcess) {
        this.preProcess = preProcess;
        return this;
    }

    @Override
    public LiteCommandsBuilder<SENDER> afterRegister(LiteCommandsProcess<SENDER> postProcess) {
        this.postProcess = postProcess;
        return this;
    }

    @Override
    public LiteCommands<SENDER> register() {
        if (registryPlatform == null) {
            throw new IllegalStateException("Registry platform is not set");
        }

        this.preProcess.process(this, this.registryPlatform, this.injectorSettings.create());

        Injector<SENDER> injector = this.injectorSettings.create();
        LiteCommands<SENDER> commands = new LiteCommandsImpl<>(senderType, registryPlatform, executeResultHandler, injector);

        if (this.commandStateFactory == null) {
            this.commandStateFactory = new LiteCommandFactory<>(injector, this.argumentsRegistry, this.editorRegistry);
        }

        for (Consumer<CommandStateFactory<SENDER>> editor : this.commandStateFactoryEditors) {
            editor.accept(this.commandStateFactory);
        }

        try {
            for (Class<?> commandsClass : this.commandsClasses) {
                Object command = injector.createInstance(commandsClass);

                this.commandsInstances.add(command);
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        CommandService<SENDER> service = commands.getCommandService();

        for (Object instance : commandsInstances) {
            Option<CommandSection<SENDER>> option = this.commandStateFactory.create(instance);

            if (option.isEmpty()) {
                continue;
            }

            service.register(option.get());
        }

        this.postProcess.process(this, this.registryPlatform, injector);

        return commands;
    }

    public static <T> LiteCommandsBuilder<T> builder(Class<T> senderType) {
        return new LiteCommandsBuilderImpl<>(senderType);
    }

}
