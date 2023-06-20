
package dev.rollczi.litecommands.annotations.async;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.CommandMetaHolder;
import dev.rollczi.litecommands.scheduler.SchedulerPollType;

public class AsyncAnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Async> {

    @Override
    public void apply(Object instance, Async annotation, CommandMetaHolder metaHolder) {
        metaHolder.editMeta(commandMeta -> commandMeta.put(CommandMeta.POLL_TYPE, SchedulerPollType.ASYNC));
    }

}
