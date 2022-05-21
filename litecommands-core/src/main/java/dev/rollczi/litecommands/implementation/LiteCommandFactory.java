package dev.rollczi.litecommands.implementation;


import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentAnnotation;
import dev.rollczi.litecommands.argument.By;
import dev.rollczi.litecommands.factory.CommandEditor;
import dev.rollczi.litecommands.factory.CommandEditorRegistry;
import dev.rollczi.litecommands.factory.CommandStateFactoryProcessor;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.factory.CommandStateFactory;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import dev.rollczi.litecommands.factory.CommandState;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.inject.Injector;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class LiteCommandFactory implements CommandStateFactory {

    private final Injector injector;
    private final ArgumentsRegistry argumentsRegistry;
    private final Set<CommandStateFactoryProcessor> processors = new HashSet<>();
    private final CommandEditorRegistry editorRegistry;

    private final Set<FactoryAnnotationResolver<?>> annotationResolvers = new HashSet<>();

    LiteCommandFactory(Injector injector, ArgumentsRegistry argumentsRegistry, CommandEditorRegistry editorRegistry) {
        this.injector = injector;
        this.argumentsRegistry = argumentsRegistry;
        this.editorRegistry = editorRegistry;
    }

    @Override
    public Option<CommandSection> create(Object instance) {
        CommandState state = this.createState(instance);

        if (state.getName() == null || state.getName().isEmpty() || state.isCanceled()) {
            return Option.none();
        }

        CommandSection section = this.stateToSection(state, instance, null);

        return Option.of(section);
    }

    @Override
    public <A extends Annotation> void annotationResolver(FactoryAnnotationResolver<A> resolver) {
        this.annotationResolvers.add(resolver);
    }

    @Override
    public <T> void editor(Class<T> on, CommandEditor editor) {
        this.editorRegistry.registerEditor(on, editor);
    }

    @Override
    public void editor(String name, CommandEditor editor) {
        this.editorRegistry.registerEditor(name, editor);
    }

    @Override
    public void stateProcessor(CommandStateFactoryProcessor processor) {
        this.processors.add(processor);
    }

    private CommandState createState(Object instance) {
        Class<?> sectionClass = instance.getClass();
        CommandState root = new CommandState()
                .cancel(true);

        for (Annotation annotation : sectionClass.getAnnotations()) {
            for (FactoryAnnotationResolver<? extends Annotation> resolver : annotationResolvers) {
                if (!annotation.annotationType().isAssignableFrom(resolver.getAnnotationClass())) {
                    continue;
                }

                Option<CommandState> resolve = resolver.tryResolve(annotation, root);

                if (resolve.isPresent()) {
                    root = resolve.get()
                            .cancel(false);
                }
            }
        }

        for (Method method : sectionClass.getDeclaredMethods()) {
            CommandState stateMethod = new CommandState()
                    .cancel(true);

            for (Annotation annotation : method.getAnnotations()) {
                for (FactoryAnnotationResolver<? extends Annotation> resolver : annotationResolvers) {
                    if (!annotation.annotationType().isAssignableFrom(resolver.getAnnotationClass())) {
                        continue;
                    }

                    Option<CommandState> resolve = resolver.tryResolve(annotation, stateMethod);

                    if (resolve.isPresent()) {
                        stateMethod = resolve.get()
                                .cancel(false);
                    }
                }
            }

            root = root.mergeMethod(method, stateMethod);
        }

        for (CommandStateFactoryProcessor processor : processors) {
            root = processor.process(root);
        }

        return (CommandState) editorRegistry.apply(sectionClass, root);
    }

    private CommandSection stateToSection(CommandState state, Object instance, @Nullable CommandSection beforeChild) {
        CommandSection section = new LiteCommandSection(state.getName(), state.getAliases());

        section.permission(state.getPermissions());
        section.excludePermission(state.getExecutedPermissions());
        section.amountValidator(state.getValidator());

        if (beforeChild != null) {
            section.applySettings(beforeChild);
        }

        for (CommandState child : state.getChildren()) {
            if (child.isCanceled()) {
                continue;
            }

            CommandSection subSection = this.stateToSection(child, instance, section);

            section.childSection(subSection);
        }

        for (Map.Entry<Method, CommandState> entry : state.getExecutors().entrySet()) {
            if (entry.getValue().isCanceled()) {
                continue;
            }

            ArgumentExecutor argumentExecutor = this.createExecutor(instance, entry.getKey(), entry.getValue());

            section.executor(argumentExecutor);
        }

        for (CommandState.Route route : state.getRoutesBefore()) {
            LiteCommandSection before = new LiteCommandSection(route.getName(), route.getAliases());

            before.childSection(section);
            section = before;
        }

        PandaStream.of(instance.getClass().getDeclaredClasses())
                .mapOpt(type -> Option.attempt(Throwable.class, () -> injector.newInstance(type)))
                .mapOpt(this::create)
                .forEach(section::childSection);

        return section;
    }

    private ArgumentExecutor createExecutor(Object object, Method method, CommandState state) {
        MethodExecutor methodExecutor = new MethodExecutor(object, method, injector);
        List<AnnotatedParameterImpl<?>> arguments = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                Class<? extends Annotation> annotationType = annotation.annotationType();

                if (!annotationType.isAnnotationPresent(ArgumentAnnotation.class)) {
                    continue;
                }

                By by = parameter.getAnnotation(By.class);
                Option<Argument<?>> argumentOption = by != null
                        ? this.argumentsRegistry.getArgument(annotationType, parameter, by.value())
                        : this.argumentsRegistry.getArgument(annotationType, parameter);

                if (argumentOption.isEmpty()) {
                    throw new IllegalArgumentException("No argument registered for annotation @" + annotationType.getSimpleName() + " and type " + parameter.getType().getSimpleName());
                }

                arguments.add(this.castAndCreateAnnotated(parameter, annotation, argumentOption.get()));
            }
        }

        return LiteArgumentArgumentExecutor.of(arguments, methodExecutor, state.getValidator());
    }

    private <A extends Annotation> AnnotatedParameterImpl<A> castAndCreateAnnotated(Parameter parameter, Annotation annotation, Argument<A> argument) {
        return new AnnotatedParameterImpl<>((A) annotation, parameter, argument);
    }

}
