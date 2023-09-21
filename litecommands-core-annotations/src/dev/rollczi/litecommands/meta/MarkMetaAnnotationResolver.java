package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;

public class MarkMetaAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(MarkMeta.class, (annotation, metaHolder) -> {
            metaHolder.meta().put(MetaKey.of(annotation.key(), String.class), annotation.value());
        });
    }
}
