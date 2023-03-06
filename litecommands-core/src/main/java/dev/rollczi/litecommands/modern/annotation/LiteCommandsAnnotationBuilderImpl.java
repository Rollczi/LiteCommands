package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommands;
import dev.rollczi.litecommands.modern.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsInternalPattern;
import dev.rollczi.litecommands.modern.annotation.editor.AnnotationCommandEditorService;
import dev.rollczi.litecommands.modern.annotation.inject.Injector;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationProcessor;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationRegistry;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.bind.BindRegistry;
import dev.rollczi.litecommands.modern.command.CommandExecuteResultResolver;
import dev.rollczi.litecommands.modern.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.editor.CommandEditorService;
import dev.rollczi.litecommands.modern.validator.CommandValidatorService;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.modern.platform.Platform;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class LiteCommandsAnnotationBuilderImpl<SENDER, B extends LiteCommandsAnnotationBuilderImpl<SENDER, B>> extends LiteCommandsBaseBuilder<SENDER, B> implements LiteCommandsAnnotationBuilder<SENDER, B> {

    private final CommandAnnotationRegistry<SENDER> commandAnnotationRegistry;
    private final AnnotationCommandEditorService<SENDER> annotationCommandEditorService;
    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;

    /**
     * Simple constructor for api usage
     *
     * @param senderClass class of sender
     */
    public LiteCommandsAnnotationBuilderImpl(Class<SENDER> senderClass) {
        this(senderClass, null);
    }

    /**
     * Simple constructor for api usage
     *
     * @param senderClass class of sender
     */
    public LiteCommandsAnnotationBuilderImpl(Class<SENDER> senderClass, Platform<SENDER> platform) {
        super(senderClass, platform);
        this.commandAnnotationRegistry = new CommandAnnotationRegistry<>();
        this.annotationCommandEditorService = new AnnotationCommandEditorService<>(this.getCommandEditorService());
        this.commandInstances = new ArrayList<>();
        this.commandClasses = new ArrayList<>();
    }

    /**
     * Pattern constructor for extensions
     *
     * @param pattern pattern to copy
     */
    public LiteCommandsAnnotationBuilderImpl(LiteCommandsInternalPattern<SENDER> pattern) {
        this(
            pattern.getSenderClass(),
            pattern.getCommandEditorService(),
            pattern.getCommandFilterService(),
            pattern.getArgumentService(),
            pattern.getBindRegistry(),
            pattern.getWrappedExpectedContextualService(),
            pattern.getResultResolver(),
            pattern.getCommandContextRegistry(),
            pattern.getPlatform(),
            new CommandAnnotationRegistry<>(),
            new AnnotationCommandEditorService<>(pattern.getCommandEditorService()),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    /**
     * Base constructor
     */
    protected LiteCommandsAnnotationBuilderImpl(
        Class<SENDER> senderClass,
        CommandEditorService<SENDER> commandEditorService,
        CommandValidatorService<SENDER> commandValidatorService,
        ArgumentService<SENDER> argumentService,
        BindRegistry<SENDER> bindRegistry,
        WrappedExpectedService wrappedExpectedService,
        CommandExecuteResultResolver<SENDER> resultResolver,
        CommandEditorContextRegistry<SENDER> commandEditorContextRegistry,
        @Nullable Platform<SENDER> platform,
        CommandAnnotationRegistry<SENDER> commandAnnotationRegistry,
        AnnotationCommandEditorService<SENDER> annotationCommandEditorService,
        List<Object> commandInstances,
        List<Class<?>> commandClasses
    ) {
        super(senderClass, commandEditorService, commandValidatorService, argumentService, bindRegistry, wrappedExpectedService, resultResolver, commandEditorContextRegistry, platform);
        this.commandAnnotationRegistry = commandAnnotationRegistry;
        this.annotationCommandEditorService = annotationCommandEditorService;
        this.commandInstances = new ArrayList<>(commandInstances);
        this.commandClasses = new ArrayList<>(commandClasses);
    }

    @Override
    public B command(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this.getThis();
    }

    @Override
    public B command(Class<?>... commands) {
        this.commandClasses.addAll(Arrays.asList(commands));
        return this.getThis();
    }

    @Override
    public <A extends Annotation> B annotation(Class<A> annotation, CommandAnnotationClassResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        return this.getThis();
    }

    @Override
    public <A extends Annotation> B annotation(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this.getThis();
    }

    @Override
    public <A extends Annotation> B annotation(Class<A> annotation, CommandAnnotationMetaApplicator<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this.getThis();
    }

    @Override
    public LiteCommands<SENDER> register() {
        CommandEditorContextRegistry<SENDER> contextRegistry = this.getCommandContextRegistry();

        CommandAnnotationProcessor<SENDER> processor = new CommandAnnotationProcessor<>(this.commandAnnotationRegistry, this.annotationCommandEditorService);
        Injector<SENDER> injector = new Injector<>(this.getBindRegistry());
        List<Object> instances = new ArrayList<>(this.commandInstances);

        for (Class<?> commandClass : this.commandClasses) {
            Object instance = injector.createInstance(commandClass);

            instances.add(instance);
        }

        for (Object instance : instances) {
            contextRegistry.register(() -> processor.createContext(instance));
        }

        return super.register();
    }

}