package dev.rollczi.litecommands.jda.visibility;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;

public class VisibilityAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Visibility.class, (annotation, metaHolder) -> metaHolder.meta()
            .listEditor(Visibility.META_KEY)
            .addAll(annotation.value())
            .apply()
        );
    }

}
