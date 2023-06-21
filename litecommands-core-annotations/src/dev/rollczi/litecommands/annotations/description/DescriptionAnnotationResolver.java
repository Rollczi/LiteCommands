package dev.rollczi.litecommands.annotations.description;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;

public class DescriptionAnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Description> {

    @Override
    public void apply(Object instance, Description annotation, MetaHolder metaHolder) {
        metaHolder.editMeta(commandMeta -> commandMeta.put(Meta.DESCRIPTION, annotation.value()));
    }

}
