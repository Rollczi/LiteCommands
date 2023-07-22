package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.argument.arg.Arg;
import dev.rollczi.litecommands.annotations.argument.arg.ArgArgumentFactory;
import dev.rollczi.litecommands.annotations.argument.flag.Flag;
import dev.rollczi.litecommands.annotations.argument.flag.FlagArgumentConfigurator;
import dev.rollczi.litecommands.annotations.argument.join.JoinArgumentConfigurator;
import dev.rollczi.litecommands.annotations.async.AsyncAnnotationResolver;
import dev.rollczi.litecommands.annotations.command.CommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.command.RootCommandAnnotationProcessor;
import dev.rollczi.litecommands.annotations.command.executor.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.annotations.command.requirement.ParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.command.requirement.NotAnnotatedParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.context.ContextParameterRequirementFactory;
import dev.rollczi.litecommands.annotations.description.DescriptionAnnotationResolver;
import dev.rollczi.litecommands.annotations.editor.AnnotationEditorService;
import dev.rollczi.litecommands.annotations.execute.ExecuteAnnotationResolver;
import dev.rollczi.litecommands.annotations.inject.Injector;
import dev.rollczi.litecommands.annotations.argument.join.Join;
import dev.rollczi.litecommands.annotations.meta.Meta;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.permission.Permissions;
import dev.rollczi.litecommands.annotations.processor.AnnotationProcessor;
import dev.rollczi.litecommands.annotations.processor.AnnotationsProcessor;
import dev.rollczi.litecommands.annotations.validator.Validate;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.LiteCommandsProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LiteAnnotationCommands<SENDER> implements LiteCommandsProvider<SENDER> {

    private final AnnotationEditorService<SENDER> annotationEditorService = new AnnotationEditorService<>();
    private final MethodCommandExecutorFactory<SENDER> commandExecutorFactory = new MethodCommandExecutorFactory<>();

    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;
    private final List<AnnotationProcessor<SENDER>> annotationProcessors;

    private LiteAnnotationCommands() {
        this.commandInstances = new ArrayList<>();
        this.commandClasses = new ArrayList<>();
        this.annotationProcessors = new ArrayList<>();
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

    public <A extends Annotation> LiteAnnotationCommands<SENDER> annotationProcessor(AnnotationProcessor<SENDER> processor) {
        this.annotationProcessors.add(processor);
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
        ContextRegistry<SENDER> contextRegistry = builder.getContextRegistry();
        ParserRegistry<SENDER> parserRegistry = builder.getParserRegistry();
        SuggesterRegistry<SENDER> suggesterRegistry = builder.getSuggesterRegistry();

        this
            // class
            .annotationProcessor(new CommandAnnotationProcessor<>())
            .annotationProcessor(new RootCommandAnnotationProcessor<>())

            // meta
            .annotationProcessor(new Meta.AnnotationResolver<>())
            .annotationProcessor(new DescriptionAnnotationResolver<>())
            .annotationProcessor(new AsyncAnnotationResolver<>())
            .annotationProcessor(new Permission.AnnotationResolver<>())
            .annotationProcessor(new Permissions.AnnotationResolver<>())
            .annotationProcessor(new Validate.AnnotationResolver<>())

            // method
            .annotationProcessor(new ExecuteAnnotationResolver<>(this.commandExecutorFactory))

            // argument
            .parameterAnnotation(Arg.class, new ArgArgumentFactory<>(wrapperRegistry, parserRegistry))
            .parameterAnnotation(Join.class, new JoinArgumentConfigurator<>(wrapperRegistry, parserRegistry, suggesterRegistry))
            .parameterAnnotation(Flag.class, new FlagArgumentConfigurator<>(wrapperRegistry, parserRegistry, suggesterRegistry))
            .parameterAnnotation(Context.class, new ContextParameterRequirementFactory<>(contextRegistry, wrapperRegistry));

        AnnotationsProcessor<SENDER> processor = new AnnotationsProcessor<>(this.annotationEditorService, builder.getEditorService(), annotationProcessors);
        Injector<SENDER> injector = new Injector<>(builder.getBindRegistry());
        List<Object> instances = new ArrayList<>(this.commandInstances);

        for (Class<?> commandClass : this.commandClasses) {
            Object instance = injector.createInstance(commandClass);

            instances.add(instance);
        }

        return instances.stream()
            .map(instance -> processor.processBuilder(instance))
            .collect(Collectors.toList());
    }

}