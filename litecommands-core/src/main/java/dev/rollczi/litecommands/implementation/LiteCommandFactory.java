package dev.rollczi.litecommands.implementation;


import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentAnnotation;
import dev.rollczi.litecommands.factory.CommandEditor;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class LiteCommandFactory implements CommandStateFactory {

    private final Injector injector;
    private final Map<Class<? extends Annotation>, Map<Class<?>, Argument<?>>> arguments = new HashMap<>();
    private final Set<CommandStateFactoryProcessor> processors = new HashSet<>();
    private final Map<Class<?>, CommandEditor> editors = new HashMap<>();

    private final Set<FactoryAnnotationResolver<?>> annotationResolvers = new HashSet<>();

    private LiteCommandFactory(Injector injector) {
        this.injector = injector;
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
    public <A extends Annotation> void argument(Class<A> annotation, Class<?> on, Argument<A> argument) {
        Map<Class<?>, Argument<?>> classArgument = this.arguments.computeIfAbsent(annotation, k -> new HashMap<>());

        classArgument.put(on, argument);
    }

    @Override
    public <A extends Annotation> void annotationResolver(FactoryAnnotationResolver<A> resolver) {
        this.annotationResolvers.add(resolver);
    }

    @Override
    public <T> void editor(Class<T> on, CommandEditor editor) {
        this.editors.put(on, editor);
    }

    @Override
    public void stateProcessor(CommandStateFactoryProcessor processor) {
        this.processors.add(processor);
    }

    private CommandState createState(Object instance) {
        Class<?> sectionClass = instance.getClass();
        CommandState root = new CommandState();

        for (Annotation annotation : sectionClass.getAnnotations()) {
            for (FactoryAnnotationResolver<? extends Annotation> resolver : annotationResolvers) {
                if (!annotation.annotationType().isAssignableFrom(resolver.getAnnotationClass())) {
                    continue;
                }

                Option<CommandState> resolve = resolver.tryResolve(annotation, root);

                if (resolve.isPresent()) {
                    root = resolve.get();
                }
            }
        }

        for (Method method : sectionClass.getDeclaredMethods()) {
            CommandState stateMethod = new CommandState();

            for (Annotation annotation : method.getAnnotations()) {
                for (FactoryAnnotationResolver<? extends Annotation> resolver : annotationResolvers) {
                    if (!annotation.annotationType().isAssignableFrom(resolver.getAnnotationClass())) {
                        continue;
                    }

                    Option<CommandState> resolve = resolver.tryResolve(annotation, stateMethod);

                    if (resolve.isPresent()) {
                        stateMethod = resolve.get();
                    }
                }
            }

            root = root.mergeMethod(method, stateMethod);
        }

        for (CommandStateFactoryProcessor processor : processors) {
            root = processor.process(root);
        }

        CommandEditor editor = editors.get(sectionClass);

        if (editor != null) {
            CommandEditor.State state = editor.edit(root);

            if (!(state instanceof CommandState)) {
                throw new IllegalStateException("edited state must be instance of CommandState.class");
            }

            root = (CommandState) state;
        }

        return root;
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
        List<AnnotatedArgument<?>> arguments = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                Class<?> annotationType = annotation.annotationType();

                if (!annotationType.isAnnotationPresent(ArgumentAnnotation.class)) {
                    continue;
                }

                Map<Class<?>, Argument<?>> classArgument = this.arguments.get(annotationType);

                if (classArgument == null) {
                    throw new IllegalArgumentException("No argument registered for annotation @" + annotationType.getSimpleName());
                }

                Argument<?> argument = null;
                Argument<?> assignableArgument = null;

                for (Map.Entry<Class<?>, Argument<?>> entry : classArgument.entrySet()) {
                    Class<?> type = entry.getKey();
                    Argument<?> argumentFromMap = entry.getValue();

                    if (argumentFromMap.canHandle(type, parameter)) {
                        argument = argumentFromMap;
                        break;
                    }

                    if (argumentFromMap.canHandleAssignableFrom(type, parameter)) {
                        assignableArgument = argumentFromMap;
                    }
                }

                Argument<? extends Annotation> finalArgument = argument == null ? assignableArgument : argument;

                if (finalArgument == null) {
                    throw new IllegalArgumentException("No argument registered for annotation @" + annotationType.getSimpleName() + " and type " + parameter.getType().getSimpleName());
                }

                arguments.add(this.castAndCreateAnnotated(parameter, annotation, finalArgument));
            }
        }

        return LiteArgumentArgumentExecutor.of(arguments, methodExecutor, state.getValidator());
    }

    private <A extends Annotation> AnnotatedArgument<A> castAndCreateAnnotated(Parameter parameter, Annotation annotation, Argument<A> argument) {
        return new AnnotatedArgument<>((A) annotation, parameter, argument);
    }

    static CommandStateFactory create(Injector injector) {
        return new LiteCommandFactory(injector);
    }

}
