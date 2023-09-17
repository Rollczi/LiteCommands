package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessorService;
import dev.rollczi.litecommands.annotation.processor.SourceProcessor;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class InstanceSourceProcessor<SENDER> implements SourceProcessor<SENDER, InstanceSource> {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final WrapperRegistry wrapperRegistry;

    public InstanceSourceProcessor(
        AnnotationProcessorService<SENDER> annotationProcessorService,
        WrapperRegistry wrapperRegistry
    ) {
        this.annotationProcessorService = annotationProcessorService;
        this.wrapperRegistry = wrapperRegistry;
    }

    @Override
    public CommandBuilder<SENDER> processBuilder(InstanceSource source) {
        Object instance = source.getInstance();
        Class<?> type = instance.getClass();
        CommandBuilder<SENDER> context = CommandBuilder.create();

        AnnotationInvoker<SENDER> classInvoker = new ClassInvoker<>(type, context);
        context = annotationProcessorService.process(classInvoker);

        for (Method method : type.getDeclaredMethods()) {
            CommandBuilderExecutor<SENDER> executorBuilder = new CommandBuilderExecutor<>(context);
            MethodInvoker<SENDER> methodInvoker = new MethodInvoker<>(instance, method, context, executorBuilder);

            context = annotationProcessorService.process(methodInvoker);

            if (!executorBuilder.buildable()) {
                continue;
            }

            int exceptedParametersCount = 1;
            for (Parameter parameter : method.getParameters()) {
                AnnotationInvoker<SENDER> parameterInvoker = new ParameterInvoker<>(wrapperRegistry, parameter, context, executorBuilder);

                context = annotationProcessorService.process(parameterInvoker);

                if (executorBuilder.getRequirementsCount() != exceptedParametersCount) { //TODO może zrobić return value flow?
                    throw new LiteCommandsReflectException(method, parameter, "Cannot find annotation processor for parameter!");
                }

                exceptedParametersCount++;
            }


        }

        return context;
    }

}
