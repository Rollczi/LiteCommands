package dev.rollczi.litecommands.annotations.validator;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

public class ValidateAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Validate.class, (annotation, metaHolder) -> {
            metaHolder.meta().listEditor(Meta.VALIDATORS)
                .addAll(annotation.value())
                .apply();
        });
    }
}
