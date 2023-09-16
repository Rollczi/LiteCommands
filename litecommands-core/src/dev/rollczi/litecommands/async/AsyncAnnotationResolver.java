
package dev.rollczi.litecommands.async;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;

public class AsyncAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Async.class, (annotation, metaHolder) -> {
            metaHolder.meta().put(Meta.POLL_TYPE, SchedulerPollType.ASYNC);

        });
    }

}

