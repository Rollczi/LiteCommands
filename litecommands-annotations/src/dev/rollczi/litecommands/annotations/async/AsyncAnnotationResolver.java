
package dev.rollczi.litecommands.annotations.async;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.scheduler.SchedulerType;

public class AsyncAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Async.class, (annotation, metaHolder) -> {
            metaHolder.meta().put(Meta.POLL_TYPE, SchedulerType.ASYNCHRONOUS);
        });
    }

}

