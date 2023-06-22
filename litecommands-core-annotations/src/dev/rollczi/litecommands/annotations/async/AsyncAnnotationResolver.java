
package dev.rollczi.litecommands.annotations.async;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;

public class AsyncAnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Async> {

    @Override
    public void apply(Object instance, Async annotation, MetaHolder metaHolder) {
        metaHolder.meta().put(Meta.POLL_TYPE, SchedulerPollType.ASYNC);
    }

}
