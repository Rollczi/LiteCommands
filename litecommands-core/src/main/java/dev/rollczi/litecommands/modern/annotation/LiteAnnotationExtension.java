package dev.rollczi.litecommands.modern.annotation;

import dev.rollczi.litecommands.modern.LiteCommandsBuilder;
import dev.rollczi.litecommands.modern.LiteCommandsExtension;
import dev.rollczi.litecommands.modern.LiteCommandsInternalPattern;
import dev.rollczi.litecommands.modern.annotation.command.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.modern.annotation.editor.AnnotationCommandEditorService;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.modern.annotation.inject.Injector;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationProcessor;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationRegistry;
import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.modern.annotation.route.RootRoute;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import dev.rollczi.litecommands.modern.argument.ArgumentService;
import dev.rollczi.litecommands.modern.editor.CommandEditorContextRegistry;
import dev.rollczi.litecommands.modern.permission.annotation.Permission;
import dev.rollczi.litecommands.modern.permission.annotation.PermissionExcluded;
import dev.rollczi.litecommands.modern.permission.annotation.Permissions;
import dev.rollczi.litecommands.modern.permission.annotation.PermissionsExcluded;
import dev.rollczi.litecommands.modern.wrapper.WrappedExpectedService;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiteAnnotationExtension<SENDER> implements LiteCommandsExtension<SENDER> {

    private final CommandAnnotationRegistry<SENDER> commandAnnotationRegistry;
    private final AnnotationCommandEditorService<SENDER> annotationCommandEditorService;
    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;

    private final List<Processor<SENDER>> beforeProcessor = new ArrayList<>();
    private final List<Processor<SENDER>> afterProcessor = new ArrayList<>();

    /**
     * Simple constructor for api usage
     */
    protected LiteAnnotationExtension(LiteAnnotatedCommands commands) {
        this.commandAnnotationRegistry = new CommandAnnotationRegistry<>();
        this.annotationCommandEditorService = new AnnotationCommandEditorService<>();
        this.commandInstances = new ArrayList<>(commands.getCommandsInstances());
        this.commandClasses = new ArrayList<>(commands.getCommandsClasses());
    }

    public LiteAnnotationExtension<SENDER> command(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationExtension<SENDER> command(Class<?>... commands) {
        this.commandClasses.addAll(Arrays.asList(commands));
        return this;
    }

    public <A extends Annotation> LiteAnnotationExtension<SENDER> annotation(Class<A> annotation, CommandAnnotationClassResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationExtension<SENDER> annotation(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationExtension<SENDER> annotation(Class<A> annotation, CommandAnnotationMetaApplicator<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public LiteAnnotationExtension<SENDER> beforeRegister(Processor<SENDER> action) {
        this.beforeProcessor.add(action);
        return this;
    }

    public LiteAnnotationExtension<SENDER> afterRegister(Processor<SENDER> action) {
        this.afterProcessor.add(action);
        return this;
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?> builder, LiteCommandsInternalPattern<SENDER> pattern) {
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
    }

    public static <SENDER> LiteAnnotationExtension<SENDER> create(LiteAnnotatedCommands commands) {
        return new LiteAnnotationExtension<SENDER>(commands).beforeRegister((extension, builder, pattern) -> {
            WrappedExpectedService service = pattern.getWrappedExpectedContextualService();
            ArgumentService<SENDER> argumentService = pattern.getArgumentService();

            MethodCommandExecutorFactory<SENDER> executorFactory = MethodCommandExecutorFactory.create(service, argumentService.getResolverRegistry());

            extension
                .annotation(Route.class, new Route.AnnotationResolver<>())
                .annotation(RootRoute.class, new RootRoute.AnnotationResolver<>())

                // method
                .annotation(Execute.class, new ExecuteAnnotationResolver<>(executorFactory))

                // meta of class and method
                .annotation(Permission.class, new Permission.AnnotationResolver<>())
                .annotation(Permissions.class, new Permissions.AnnotationResolver<>())
                .annotation(PermissionExcluded.class, new PermissionExcluded.AnnotationResolver<>())
                .annotation(PermissionsExcluded.class, new PermissionsExcluded.AnnotationResolver<>());
        });
    }

    public static <SENCER> LiteAnnotationExtension<SENCER> create() {
        return create(new LiteAnnotatedCommands());
    }

    interface Processor<SENDER> {
        void process(LiteAnnotationExtension<SENDER> extension, LiteCommandsBuilder<SENDER, ?> builder, LiteCommandsInternalPattern<SENDER> pattern);
    }

}