package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.arg.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.async.AsyncAnnotationResolver;
import dev.rollczi.litecommands.annotations.command.executor.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.NotAnnotatedParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.context.ContextParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.description.DescriptionAnnotationResolver;
import dev.rollczi.litecommands.annotations.editor.AnnotationEditorService;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.annotations.inject.Injector;
import dev.rollczi.litecommands.annotations.argument.join.Join;
import dev.rollczi.litecommands.annotations.meta.Meta;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.permission.Permissions;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMethodResolver;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.processor.CommandAnnotationRegistry;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.validator.Validate;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.LiteCommandsProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LiteAnnotationCommands<SENDER> implements LiteCommandsProvider<SENDER> {

    private final CommandAnnotationRegistry<SENDER> commandAnnotationRegistry = new CommandAnnotationRegistry<>();
    private final AnnotationEditorService<SENDER> annotationEditorService = new AnnotationEditorService<>();
    private final MethodCommandExecutorFactory<SENDER> commandExecutorFactory = new MethodCommandExecutorFactory<>();

    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;

    private LiteAnnotationCommands() {
        this.commandInstances = new ArrayList<>();
        this.commandClasses = new ArrayList<>();
    }

    public LiteAnnotationCommands<SENDER> load(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationCommands<SENDER> loadClasses(Class<?>... commands) {
        this.commandClasses.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationCommands<SENDER> loadPackages(String... packageNames) {
        // TODO implement
        return this;
    }

    public LiteAnnotationCommands<SENDER> loadPackages(Package... packages) {
        // TODO implement
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommands<SENDER> annotation(Class<A> annotation, CommandAnnotationClassResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommands<SENDER> annotation(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommands<SENDER> annotation(Class<A> annotation, CommandAnnotationMetaApplicator<SENDER, A> resolver) {
        this.commandAnnotationRegistry.registerResolver(annotation, resolver);
        this.commandAnnotationRegistry.registerMethodResolver(annotation, resolver);
        return this;
    }

    public <A extends Annotation> LiteAnnotationCommands<SENDER> parameterAnnotation(Class<A> annotation, ParameterRequirementFactory<SENDER, A> resolver) {
        this.commandExecutorFactory.registerResolver(annotation, resolver);
        return this;
    }

    public LiteAnnotationCommands<SENDER> parameterWithoutAnnotation(NotAnnotatedParameterRequirementFactory<SENDER> resolver) {
        this.commandExecutorFactory.defaultResolver(resolver);
        return this;
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> create() {
        return new LiteAnnotationCommands<>();
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> of(Object... commands) {
        return new LiteAnnotationCommands<SENDER>().load(commands);
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> ofClasses(Class<?>... commands) {
        return new LiteAnnotationCommands<SENDER>().loadClasses(commands);
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> ofPackages(String... packageNames) {
        return new LiteAnnotationCommands<SENDER>().loadPackages(packageNames);
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> ofPackages(Package... packages) {
        return new LiteAnnotationCommands<SENDER>().loadPackages(packages);
    }

    @Override
    public List<CommandBuilder<SENDER>> provide(LiteCommandsInternalBuilderApi<SENDER, ?> builder) {
        WrapperRegistry wrapperRegistry = builder.getWrapperRegistry();
        ParserRegistry<SENDER> parserRegistry = builder.getArgumentParserService();

        this
            .annotation(Command.class, new Command.AnnotationResolver<>())
            .annotation(RootCommand.class, new RootCommand.AnnotationResolver<>())

            .annotation(Meta.class, new Meta.AnnotationResolver<>())
            .annotation(Description.class, new DescriptionAnnotationResolver<>())
            .annotation(Async.class, new AsyncAnnotationResolver<>())
            .annotation(Permission.class, new Permission.AnnotationResolver<>())
            .annotation(Permissions.class, new Permissions.AnnotationResolver<>())
            .annotation(Validate.class, new Validate.AnnotationResolver<>())

            // method
            .annotation(Execute.class, new ExecuteAnnotationResolver<>(this.commandExecutorFactory))

            // argument
            .parameterAnnotation(Arg.class, new Arg.Factory<>(wrapperRegistry, parserRegistry))
            .parameterAnnotation(Join.class, new Join.Factory<>(wrapperRegistry, parserRegistry))
            .parameterAnnotation(Context.class, new ContextParameterRequirementFactory<>(builder.getContextRegistry(), wrapperRegistry));



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