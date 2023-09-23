package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessorService;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Method;

class InstanceSourceProcessor<SENDER> {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final WrapperRegistry wrapperRegistry;

    public InstanceSourceProcessor(
        AnnotationProcessorService<SENDER> annotationProcessorService,
        WrapperRegistry wrapperRegistry
    ) {
        this.annotationProcessorService = annotationProcessorService;
        this.wrapperRegistry = wrapperRegistry;
    }

    public CommandBuilder<SENDER> processBuilder(InstanceSource source) {
        Object instance = source.getInstance();
        Class<?> type = instance.getClass();
        CommandBuilder<SENDER> context = CommandBuilder.create();

        AnnotationInvoker<SENDER> classInvoker = new ClassInvoker<>(type, context);
        context = annotationProcessorService.process(classInvoker);

        for (Method method : type.getDeclaredMethods()) {
            MethodInvoker<SENDER> methodInvoker = new MethodInvoker<>(annotationProcessorService, wrapperRegistry, instance, method, context);

            context = annotationProcessorService.process(methodInvoker);
        }

        return context;
    }

}
