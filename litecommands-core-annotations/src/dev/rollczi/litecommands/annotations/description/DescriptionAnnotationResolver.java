package dev.rollczi.litecommands.annotations.description;

import dev.rollczi.litecommands.annotations.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

public class DescriptionAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onAnnotatedMetaHolder(Description.class, (instance, annotation, metaHolder) -> {
            metaHolder.meta().put(Meta.DESCRIPTION, annotation.value());

        });
    }

}

