package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.annotations.command.executor.MethodCommandExecutorFactory;
import dev.rollczi.litecommands.annotations.editor.AnnotationEditorService;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.editor.EditorService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class AnnotationsProcessor<SENDER> {

    private final List<AnnotationProcessor<SENDER>> annotationProcessors = new ArrayList<>();
    private final AnnotationEditorService<SENDER> annotationCommandEditorRegistry;
    private final EditorService<SENDER> editorService;
    private final MethodCommandExecutorFactory<SENDER> methodCommandExecutorFactory;

    public AnnotationsProcessor(AnnotationEditorService<SENDER> annotationCommandEditorRegistry, EditorService<SENDER> editorService, List<AnnotationProcessor<SENDER>> annotationProcessors, MethodCommandExecutorFactory<SENDER> methodCommandExecutorFactory) {
        this.methodCommandExecutorFactory = methodCommandExecutorFactory;
        this.annotationProcessors.addAll(annotationProcessors);
        this.annotationCommandEditorRegistry = annotationCommandEditorRegistry;
        this.editorService = editorService;
    }

    public CommandBuilder<SENDER> processBuilder(Object instance) {
        Class<?> type = instance.getClass();
        CommandBuilder<SENDER> context = CommandBuilder.create();

        AnnotationInvoker<SENDER> classInvoker = new ClassInvoker<>(type, context);

        for (AnnotationProcessor<SENDER> processor : annotationProcessors) {
            classInvoker = processor.process(classInvoker);
        }

        context = classInvoker.getResult();

        for (Method method : type.getDeclaredMethods()) {
            CommandBuilderExecutor<SENDER> executorBuilder = new CommandBuilderExecutor<>(context);
            AnnotationInvoker<SENDER> methodInvoker = new MethodInvoker<>(instance, method, context, executorBuilder, methodCommandExecutorFactory);

            for (AnnotationProcessor<SENDER> processor : annotationProcessors) {
                methodInvoker = processor.process(methodInvoker);
            }

            context = methodInvoker.getResult();

            for (Parameter parameter : method.getParameters()) {
                AnnotationInvoker<SENDER> parameterInvoker = new ParameterInvoker<>(instance, method, parameter, context, executorBuilder);

                for (AnnotationProcessor<SENDER> processor : annotationProcessors) {
                    parameterInvoker = processor.process(parameterInvoker);
                }

                context = parameterInvoker.getResult();
            }
        }

        context = this.annotationCommandEditorRegistry.edit(instance.getClass(), context);
        context = this.editorService.edit(context);

        return context;
    }

}
