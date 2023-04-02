package dev.rollczi.litecommands.annotations.description;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.CommandMetaHolder;

public class DescriptionAnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Description> {

    @Override
    public void apply(Object instance, Description annotation, CommandMetaHolder metaHolder) {
        metaHolder.editMeta(commandMeta -> commandMeta.put(CommandMeta.DESCRIPTION, annotation.value()));
    }

}
