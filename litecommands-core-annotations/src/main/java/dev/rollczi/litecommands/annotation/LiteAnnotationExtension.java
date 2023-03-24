package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.annotation.argument.Arg;
import dev.rollczi.litecommands.annotation.argument.ArgAnnotationResolver;
import dev.rollczi.litecommands.annotation.command.MethodCommandExecutorService;
import dev.rollczi.litecommands.annotation.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.annotation.command.ParameterWithoutAnnotationResolver;
import dev.rollczi.litecommands.annotation.context.Context;
import dev.rollczi.litecommands.annotation.context.ContextAnnotationResolver;
import dev.rollczi.litecommands.annotation.execute.Execute;
import dev.rollczi.litecommands.annotation.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.annotation.permission.Permission;
import dev.rollczi.litecommands.annotation.permission.PermissionExcluded;
import dev.rollczi.litecommands.annotation.permission.Permissions;
import dev.rollczi.litecommands.annotation.permission.PermissionsExcluded;
import dev.rollczi.litecommands.annotation.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.annotation.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.annotation.processor.CommandAnnotationProcessor;
import dev.rollczi.litecommands.annotation.processor.CommandAnnotationRegistry;
import dev.rollczi.litecommands.argument.ArgumentResolverRegistry;
import dev.rollczi.litecommands.builder.LiteCommandsBuilder;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.extension.LiteCommandsExtension;
import dev.rollczi.litecommands.annotation.editor.AnnotationCommandEditorService;
import dev.rollczi.litecommands.annotation.inject.Injector;
import dev.rollczi.litecommands.annotation.route.RootRoute;
import dev.rollczi.litecommands.annotation.route.Route;
import dev.rollczi.litecommands.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.platform.LiteSettings;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LiteAnnotationExtension<SENDER, C extends LiteSettings> implements LiteCommandsExtension<SENDER, C> {

    private final CommandAnnotationRegistry<SENDER> commandAnnotationRegistry = new CommandAnnotationRegistry<>();
    private final AnnotationCommandEditorService<SENDER> annotationCommandEditorService = new AnnotationCommandEditorService<>();
    private final MethodCommandExecutorService<SENDER> commandExecutorFactory = new MethodCommandExecutorService<>();

    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;

    private final List<Processor<SENDER>> beforeProcessor = new ArrayList<>();
    private final List<Processor<SENDER>> afterProcessor = new ArrayList<>();

    public LiteAnnotationExtension() {
        this.commandInstances = new ArrayList<>();
        this.commandClasses = new ArrayList<>();
    }

    protected LiteAnnotationExtension(Builder commands) {
        this.commandInstances = new ArrayList<>(commands.getCommandsInstances());
        this.commandClasses = new ArrayList<>(commands.getCommandsClasses());
    }

    public LiteAnnotationExtension<SENDER, C> command(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationExtension<SENDER, C> command(Class<?>... commands) {
        this.commandClasses.addAll(Arrays.asList(commands));
        return this;
    }

    public <A extends Annotation> LiteAnnotationExtension<SENDER, C> annotation(Class<A> annotation, CommandAnnotationClassResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationExtension<SENDER, C> annotation(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationExtension<SENDER, C> annotation(Class<A> annotation, CommandAnnotationMetaApplicator<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationExtension<SENDER, C> parameterAnnotation(Class<A> annotation, ParameterWithAnnotationResolver<SENDER, A> resolver) {
        this.commandExecutorFactory.registerResolver(annotation, resolver);
        return this;
    }

    public LiteAnnotationExtension<SENDER, C> parameterWithoutAnnotation(ParameterWithoutAnnotationResolver<SENDER> resolver) {
        this.commandExecutorFactory.defaultResolver(resolver);
        return this;
    }

    public LiteAnnotationExtension<SENDER, C> beforeRegister(Processor<SENDER> action) {
        this.beforeProcessor.add(action);
        return this;
    }

    public LiteAnnotationExtension<SENDER, C> afterRegister(Processor<SENDER> action) {
        this.afterProcessor.add(action);
        return this;
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, C, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern) {
        for (Processor<SENDER> action : this.beforeProcessor) {
            action.process(this, builder, pattern);
        }

        CommandEditorContextRegistry<SENDER> contextRegistry = pattern.getCommandContextRegistry();

        CommandAnnotationProcessor<SENDER> processor = new CommandAnnotationProcessor<>(this.commandAnnotationRegistry, this.annotationCommandEditorService, pattern.getCommandEditorService());
        Injector<SENDER> injector = new Injector<>(pattern.getBindRegistry());
        List<Object> instances = new ArrayList<>(this.commandInstances);

        for (Class<?> commandClass : this.commandClasses) {
            Object instance = injector.createInstance(commandClass);

            instances.add(instance);
        }

        for (Object instance : instances) {
            contextRegistry.register(() -> processor.createContext(instance));
        }

        for (Processor<SENDER> action : this.afterProcessor) {
            action.process(this, builder, pattern);
        }
    }

    public static <SENCER, C extends LiteSettings> LiteAnnotationExtension<SENCER, C> create() {
        return create(new Builder());
    }

    public static <SENDER, C extends LiteSettings> LiteAnnotationExtension<SENDER, C> create(Builder commands) {
        return new LiteAnnotationExtension<SENDER, C>(commands).beforeRegister((extension, builder, pattern) -> {
            WrappedExpectedService wrappedExpectedService = pattern.getWrappedExpectedContextualService();
            ArgumentResolverRegistry<SENDER> argumentResolverRegistry = pattern.getArgumentService().getResolverRegistry();

            extension.command()
                // class or method
                .annotation(Route.class, new Route.AnnotationResolver<>())
                .annotation(RootRoute.class, new RootRoute.AnnotationResolver<>())

                .annotation(Permission.class, new Permission.AnnotationResolver<>())
                .annotation(Permissions.class, new Permissions.AnnotationResolver<>())
                .annotation(PermissionExcluded.class, new PermissionExcluded.AnnotationResolver<>())
                .annotation(PermissionsExcluded.class, new PermissionsExcluded.AnnotationResolver<>())

                // method
                .annotation(Execute.class, new ExecuteAnnotationResolver<>(extension.commandExecutorFactory))

                // argument
                .parameterAnnotation(Arg.class, new ArgAnnotationResolver<>(wrappedExpectedService, argumentResolverRegistry))
                .parameterAnnotation(Context.class, new ContextAnnotationResolver<>(pattern.getBindRegistry(), wrappedExpectedService))
            ;
        });
    }

    interface Processor<SENDER> {
        void process(LiteAnnotationExtension<SENDER, ?> extension, LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternalBuilderApi<SENDER, ?> pattern);
    }

    public static class Builder {

        private final List<Object> commandsInstances = new ArrayList<>();
        private final List<Class<?>> commandsClasses = new ArrayList<>();

        public Builder command(Object... commands) {
            for (Object command : commands) {
                if (command == null) {
                    throw new IllegalArgumentException("Command cannot be null");
                }

                if (command instanceof Class<?>) {
                    commandsClasses.add((Class<?>) command);
                    continue;
                }

                commandsInstances.add(command);
            }

            return this;
        }

        public Builder command(Class<?>... commands) {
            for (Class<?> command : commands) {
                if (command == null) {
                    throw new IllegalArgumentException("Command cannot be null");
                }

                commandsClasses.add(command);
            }

            return this;
        }

        List<Object> getCommandsInstances() {
            return Collections.unmodifiableList(commandsInstances);
        }

        List<Class<?>> getCommandsClasses() {
            return Collections.unmodifiableList(commandsClasses);
        }

        public <SENDER, C extends LiteSettings> LiteAnnotationExtension<SENDER, C> build() {
            return new LiteAnnotationExtension<>(this);
        }

    }
}