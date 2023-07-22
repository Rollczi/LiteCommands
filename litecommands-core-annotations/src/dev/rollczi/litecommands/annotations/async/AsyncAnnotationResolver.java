
package dev.rollczi.litecommands.annotations.async;

import dev.rollczi.litecommands.annotations.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;

public class AsyncAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onAnnotatedMetaHolder(Async.class, (instance, annotation, metaHolder) -> {
            metaHolder.meta().put(Meta.POLL_TYPE, SchedulerPollType.ASYNC);

        });
    }

}

