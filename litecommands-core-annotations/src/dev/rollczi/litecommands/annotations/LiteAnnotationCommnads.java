package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.ArgAnnotationResolver;
import dev.rollczi.litecommands.annotations.command.MethodCommandExecutorService;
import dev.rollczi.litecommands.annotations.command.ParameterWithAnnotationResolver;
import dev.rollczi.litecommands.annotations.command.ParameterWithoutAnnotationResolver;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.context.ContextAnnotationResolver;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.description.DescriptionAnnotationResolver;
import dev.rollczi.litecommands.annotations.editor.AnnotationEditorService;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.annotations.inject.Injector;
import dev.rollczi.litecommands.annotations.meta.Meta;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.permission.Permissions;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationRegistry;
import dev.rollczi.litecommands.annotations.route.RootRoute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.annotations.validator.Validate;
import dev.rollczi.litecommands.argument.parser.ArgumentParserRegistry;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.LiteCommandsProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LiteAnnotationCommnads<SENDER> implements LiteCommandsProvider<SENDER> {

    private final CommandAnnotationRegistry<SENDER> commandAnnotationRegistry = new CommandAnnotationRegistry<>();
    private final AnnotationEditorService<SENDER> annotationEditorService = new AnnotationEditorService<>();
    private final MethodCommandExecutorService<SENDER> commandExecutorFactory = new MethodCommandExecutorService<>();

    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;

    private LiteAnnotationCommnads() {
        this.commandInstances = new ArrayList<>();
        this.commandClasses = new ArrayList<>();
    }

    public LiteAnnotationCommnads<SENDER> load(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationCommnads<SENDER> loadClasses(Class<?>... commands) {
        this.commandClasses.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationCommnads<SENDER> loadPackages(String... packageNames) {
        // TODO implement
        return this;
    }

    public LiteAnnotationCommnads<SENDER> loadPackages(Package... packages) {
        // TODO implement
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommnads<SENDER> annotation(Class<A> annotation, CommandAnnotationClassResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommnads<SENDER> annotation(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommnads<SENDER> annotation(Class<A> annotation, CommandAnnotationMetaApplicator<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommnads<SENDER> parameterAnnotation(Class<A> annotation, ParameterWithAnnotationResolver<SENDER, A> resolver) {
        this.commandExecutorFactory.registerResolver(annotation, resolver);
        return this;
    }

    public LiteAnnotationCommnads<SENDER> parameterWithoutAnnotation(ParameterWithoutAnnotationResolver<SENDER> resolver) {
        this.commandExecutorFactory.defaultResolver(resolver);
        return this;
    }

    public static <SENDER> LiteAnnotationCommnads<SENDER> create() {
        return new LiteAnnotationCommnads<>();
    }

    public static <SENDER> LiteAnnotationCommnads<SENDER> of(Object... commands) {
        return new LiteAnnotationCommnads<SENDER>().load(commands);
    }

    public static <SENDER> LiteAnnotationCommnads<SENDER> ofClasses(Class<?>... commands) {
        return new LiteAnnotationCommnads<SENDER>().loadClasses(commands);
    }

    public static <SENDER> LiteAnnotationCommnads<SENDER> ofPackages(String... packageNames) {
        return new LiteAnnotationCommnads<SENDER>().loadPackages(packageNames);
    }

    public static <SENDER> LiteAnnotationCommnads<SENDER> ofPackages(Package... packages) {
        return new LiteAnnotationCommnads<SENDER>().loadPackages(packages);
    }

    @Override
    public List<CommandBuilder<SENDER>> provide(LiteCommandsInternalBuilderApi<SENDER, ?> builder) {
        WrapperRegistry wrapperRegistry = builder.getWrapperRegistry();
        ArgumentParserRegistry<SENDER> argumentParserRegistry = builder.getArgumentParserService();

        this
            .annotation(Route.class, new Route.AnnotationResolver<>())
            .annotation(RootRoute.class, new RootRoute.AnnotationResolver<>())

            .annotation(Meta.class, new Meta.AnnotationResolver<>())
            .annotation(Description.class, new DescriptionAnnotationResolver<>())
            .annotation(Permission.class, new Permission.AnnotationResolver<>())
            .annotation(Permissions.class, new Permissions.AnnotationResolver<>())
            .annotation(Validate.class, new Validate.AnnotationResolver<>())

            // method
            .annotation(Execute.class, new ExecuteAnnotationResolver<>(this.commandExecutorFactory))

            // argument
            .parameterAnnotation(Arg.class, new ArgAnnotationResolver<>(wrapperRegistry, argumentParserRegistry))
            .parameterAnnotation(Context.class, new ContextAnnotationResolver<>(builder.getContextRegistry(), wrapperRegistry));

        CommandAnnotationProcessor<SENDER> processor = new CommandAnnotationProcessor<>(this.commandAnnotationRegistry, this.annotationEditorService, builder.getEditorService());
        Injector<SENDER> injector = new Injector<>(builder.getBindRegistry());
        List<Object> instances = new ArrayList<>(this.commandInstances);

        for (Class<?> commandClass : this.commandClasses) {
            Object instance = injector.createInstance(commandClass);

            instances.add(instance);
        }

        return instances.stream()
            .map(instance -> processor.createContext(instance))
            .collect(Collectors.toList());
    }

}