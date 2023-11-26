package dev.rollczi.litecommands.annotations.description;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DescriptionAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Description.class, (annotation, metaHolder) -> {
            metaHolder.meta().put(Meta.DESCRIPTION, Arrays.stream(annotation.value()).collect(Collectors.toList()));
        });
    }

}

