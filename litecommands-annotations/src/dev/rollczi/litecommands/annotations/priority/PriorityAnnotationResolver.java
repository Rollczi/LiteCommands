package dev.rollczi.litecommands.annotations.priority;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.priority.PriorityLevel;

public class PriorityAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Priority.class, (annotation, metaHolder) -> {
            if (annotation.custom() != 0) {
                metaHolder.meta().put(Meta.PRIORITY, new PriorityLevel("CUSTOM", annotation.custom()));
                return;
            }

            metaHolder.meta().put(Meta.PRIORITY, annotation.value().toPriorityLevel());
        });
    }

}
