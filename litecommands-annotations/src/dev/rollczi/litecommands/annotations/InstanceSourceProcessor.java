package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorService;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Method;

class InstanceSourceProcessor<SENDER> {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final MethodValidatorService<SENDER> validatorService;
    private final WrapperRegistry wrapperRegistry;

    public InstanceSourceProcessor(
        AnnotationProcessorService<SENDER> annotationProcessorService,
        MethodValidatorService<SENDER> validatorService, WrapperRegistry wrapperRegistry
    ) {
        this.annotationProcessorService = annotationProcessorService;
        this.validatorService = validatorService;
        this.wrapperRegistry = wrapperRegistry;
    }

    public CommandBuilder<SENDER> processBuilder(InstanceSource source) {
        Object instance = source.getInstance();
        Class<?> type = instance.getClass();
        CommandBuilder<SENDER> context = CommandBuilder.<SENDER>create()
            .applyMeta(meta -> meta.put(Meta.COMMAND_ORIGIN_TYPE, type));

        AnnotationInvoker<SENDER> classInvoker = new ClassInvoker<>(type, context);
        context = annotationProcessorService.process(classInvoker);

        for (Method method : type.getDeclaredMethods()) {
            MethodInvoker<SENDER> methodInvoker = new MethodInvoker<>(annotationProcessorService, validatorService, wrapperRegistry, instance, method, context);

            context = annotationProcessorService.process(methodInvoker);
        }

        return context;
    }

}
