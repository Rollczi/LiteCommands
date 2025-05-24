package dev.rollczi.litecommands.jda.integration;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;

public class IntegrationAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Integration.class, (annotation, metaHolder) -> metaHolder.meta()
            .listEditor(Integration.META_KEY)
            .addAll(annotation.value())
            .apply()
        );
    }

}
