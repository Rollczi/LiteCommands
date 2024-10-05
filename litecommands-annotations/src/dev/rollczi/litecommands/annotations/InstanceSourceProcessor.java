package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorService;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.reflect.ReflectUtil;

import java.lang.reflect.Method;

class InstanceSourceProcessor<SENDER> {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final MethodValidatorService<SENDER> validatorService;

    public InstanceSourceProcessor(
        AnnotationProcessorService<SENDER> annotationProcessorService,
        MethodValidatorService<SENDER> validatorService
    ) {
        this.annotationProcessorService = annotationProcessorService;
        this.validatorService = validatorService;
    }

    public CommandBuilder<SENDER> processBuilder(InstanceSource source) {
        Object instance = source.getInstance();
        Class<?> type = instance.getClass();
        CommandBuilder<SENDER> context = CommandBuilder.<SENDER>create()
            .applyMeta(meta -> meta.list(Meta.COMMAND_ORIGIN_TYPE, list -> list.add(type)));

        AnnotationInvoker<SENDER> classInvoker = new ClassInvoker<>(type, context);
        context = annotationProcessorService.process(classInvoker);

        for (Method method : ReflectUtil.getMethods(type)) {
            MethodInvoker<SENDER> methodInvoker = new MethodInvoker<>(annotationProcessorService, validatorService, instance, method, context);

            context = annotationProcessorService.process(methodInvoker);
        }

        return context;
    }

}
