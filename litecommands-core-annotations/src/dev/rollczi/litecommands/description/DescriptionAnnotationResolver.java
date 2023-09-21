package dev.rollczi.litecommands.description;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

public class DescriptionAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Description.class, (annotation, metaHolder) -> {
            metaHolder.meta().put(Meta.DESCRIPTION, annotation.value());

        });
    }

}

