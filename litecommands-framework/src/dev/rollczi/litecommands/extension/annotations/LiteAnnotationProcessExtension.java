package dev.rollczi.litecommands.extension.annotations;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.LiteCommandsProvider;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.annotations.AnnotationProcessorService;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidatorProcessor;
import dev.rollczi.litecommands.extension.LiteCommandsProviderExtension;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class LiteAnnotationProcessExtension<SENDER> implements LiteCommandsProviderExtension<SENDER> {

    private final List<AnnotationProcessor<SENDER>> processors = new ArrayList<>();

    @Override
    public void extendCommandsProvider(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal, LiteCommandsProvider<SENDER> provider) {
        if (!(provider instanceof LiteCommandsAnnotations)) {
            return;
        }

        LiteCommandsAnnotations<SENDER> annotations = (LiteCommandsAnnotations<SENDER>) provider;
        AnnotationProcessorService<SENDER> annotationProcessorService = annotations.getAnnotationProcessorService();

        for (AnnotationProcessor<SENDER> processor : processors) {
            annotationProcessorService.register(processor);
        }
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
    }

    public LiteAnnotationProcessExtension<SENDER> processor(AnnotationProcessor<SENDER> processor) {
        processors.add(processor);
        return this;
    }

    public <T, A extends Annotation> LiteAnnotationProcessExtension<SENDER> validator(Class<T> type, Class<A> annotation, AnnotatedValidator<SENDER, T, A> validator) {
        return processor(new AnnotatedValidatorProcessor<>(annotation, type, validator));
    }

}
