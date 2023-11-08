package dev.rollczi.litecommands.annotations.meta;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.MetaKey;

public class MarkMetaAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(MarkMeta.class, (annotation, metaHolder) -> {
            metaHolder.meta().put(MetaKey.of(annotation.key(), String.class, null, annotation.copyToFastCommand()), annotation.value());
        });
    }
}
