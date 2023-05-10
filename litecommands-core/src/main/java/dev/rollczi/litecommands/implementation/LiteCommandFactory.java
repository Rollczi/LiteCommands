package dev.rollczi.litecommands.implementation;


import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.By;
import dev.rollczi.litecommands.command.execute.ArgumentExecutor;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.factory.CommandEditor;
import dev.rollczi.litecommands.factory.CommandEditorRegistry;
import dev.rollczi.litecommands.factory.CommandState;
import dev.rollczi.litecommands.factory.CommandStateFactory;
import dev.rollczi.litecommands.factory.CommandStateFactoryProcessor;
import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;
import dev.rollczi.litecommands.injector.InjectException;
import dev.rollczi.litecommands.injector.Injectable;
import dev.rollczi.litecommands.injector.Injector;
import dev.rollczi.litecommands.meta.CommandMeta;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class LiteCommandFactory<SENDER> implements CommandStateFactory<SENDER> {

    private final Injector<SENDER> injector;
    private final ArgumentsRegistry<SENDER> argumentsRegistry;
    private final Set<CommandStateFactoryProcessor> processors = new HashSet<>();
    private final CommandEditorRegistry editorRegistry;

    private final Set<FactoryAnnotationResolver<?>> annotationResolvers = new HashSet<>();

    LiteCommandFactory(Injector<SENDER> injector, ArgumentsRegistry<SENDER> argumentsRegistry, CommandEditorRegistry editorRegistry) {
        this.injector = injector;
        this.argumentsRegistry = argumentsRegistry;
        this.editorRegistry = editorRegistry;
    }

    @Override
    public Option<List<CommandSection<SENDER>>> create(Object instance) {
        CommandState state = this.createState(instance);

        if (state.getName() == null || state.isCanceled()) {
            return Option.none();
        }

        CommandSection<SENDER> section = this.stateToSection(state, instance);
        List<CommandSection<SENDER>> sections = this.unpackEmptySection(section);

        return Option.of(sections);
    }

    private List<CommandSection<SENDER>> unpackEmptySection(CommandSection<SENDER> section) {
        if (section.getName().isEmpty()) {
            if (!section.executors().isEmpty()) {
                throw new IllegalStateException("Empty section cannot have executors without name");
            }

            return section.childrenSection().stream()
                .flatMap(child -> this.unpackEmptySection(child).stream())
                .peek(unpackedSection -> unpackedSection.meta().applyCommandMeta(section.meta()))
                .collect(Collectors.toList());
        }

        return Collections.singletonList(section);
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
        Class<?> commandClass = instance.getClass();
        CommandState rootCommandState = new CommandState()
                .cancel(true);

        for (Annotation annotation : commandClass.getAnnotations()) {
            for (FactoryAnnotationResolver<? extends Annotation> resolver : annotationResolvers) {
                if (!annotation.annotationType().equals(resolver.getAnnotationClass())) {
                    continue;
                }

                Option<CommandState> resolve = resolver.tryResolve(annotation, rootCommandState);

                if (resolve.isPresent()) {
                    rootCommandState = resolve.get()
                            .cancel(false);
                }
            }
        }

        for (Method method : commandClass.getDeclaredMethods()) {
            CommandState potentialMethodState = new CommandState()
                    .cancel(true);

            for (Annotation annotation : method.getAnnotations()) {
                for (FactoryAnnotationResolver<? extends Annotation> resolver : annotationResolvers) {
                    if (!annotation.annotationType().equals(resolver.getAnnotationClass())) {
                        continue;
                    }

                    Option<CommandState> resolve = resolver.tryResolve(annotation, potentialMethodState);

                    if (resolve.isPresent()) {
                        potentialMethodState = resolve.get()
                                .cancel(false);
                    }
                }
            }

            rootCommandState = rootCommandState.mergeMethod(method, potentialMethodState);
        }

        for (CommandStateFactoryProcessor processor : processors) {
            rootCommandState = processor.process(rootCommandState);
        }

        return (CommandState) editorRegistry.apply(commandClass, rootCommandState);
    }

    private CommandSection<SENDER> stateToSection(CommandState state, Object instance) {
        CommandSection<SENDER> section = new LiteCommandSection<>(state.getName(), state.getAliases());

        section.meta().applyCommandMeta(state.getMeta());
        section = this.resolveStateOnSection(section, instance, state);

        PandaStream.of(instance.getClass().getDeclaredClasses())
            .mapOpt(type -> Option.supplyThrowing(InjectException.class, () -> this.injector.createInstance(type)))
            .mapOpt(this::create)
            .flatMap(commandSections -> commandSections)
            .forEach(section::childSection);

        return section;
    }

    private CommandSection<SENDER> stateToSection(CommandState state, Object instance, CommandMeta before) {
        CommandSection<SENDER> section = new LiteCommandSection<>(state.getName(), state.getAliases());

        section.meta().applyCommandMeta(state.getMeta());
        section.meta().applyCommandMeta(before);
        section = this.resolveStateOnSection(section, instance, state);

        return section;
    }

    private CommandSection<SENDER> resolveStateOnSection(CommandSection<SENDER> section, Object instance, CommandState state) {
        for (CommandState child : state.getChildren()) {
            if (child.isCanceled()) {
                continue;
            }

            CommandSection<SENDER> subSection = this.stateToSection(child, instance, section.meta());

            section.childSection(subSection);
        }

        for (Map.Entry<Method, CommandState> entry : state.getExecutors().entrySet()) {
            if (entry.getValue().isCanceled()) {
                continue;
            }

            ArgumentExecutor<SENDER> argumentExecutor = this.createExecutor(instance, entry.getKey(), entry.getValue());

            section.executor(argumentExecutor);
        }

        for (CommandState.Route route : state.getRoutesBefore()) {
            LiteCommandSection<SENDER> before = new LiteCommandSection<>(route.getName(), route.getAliases());

            before.childSection(section);
            section = before;
        }

        return section;
    }

    private ArgumentExecutor<SENDER> createExecutor(Object object, Method method, CommandState state) {
        MethodExecutor<SENDER> methodExecutor = new MethodExecutor<>(object, method, injector);
        List<AnnotatedParameterImpl<SENDER, ?>> arguments = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                Class<? extends Annotation> annotationType = annotation.annotationType();

                if (!annotationType.isAnnotationPresent(Injectable.class)) {
                    continue;
                }

                By by = parameter.getAnnotation(By.class);
                Option<Argument<SENDER, ?>> argumentOption = by != null
                        ? this.argumentsRegistry.getArgument(annotationType, parameter, by.value())
                        : this.argumentsRegistry.getArgument(annotationType, parameter);

                if (argumentOption.isEmpty()) {
                    throw new IllegalArgumentException("No argument registered for annotation @" + annotationType.getSimpleName() + " and type " + parameter.getType().getSimpleName());
                }

                arguments.add(this.castAndCreateAnnotated(parameter, annotation, argumentOption.get()));
            }
        }

        LiteArgumentExecutor<SENDER> executor = LiteArgumentExecutor.of(arguments, methodExecutor);

        executor.meta().applyCommandMeta(state.getMeta());

        return executor;
    }

    @SuppressWarnings("unchecked")
    private <T, A extends Annotation> AnnotatedParameterImpl<T, A> castAndCreateAnnotated(Parameter parameter, Annotation annotation, Argument<T, A> argument) {
        return new AnnotatedParameterImpl<>((A) annotation, parameter, argument);
    }

}
