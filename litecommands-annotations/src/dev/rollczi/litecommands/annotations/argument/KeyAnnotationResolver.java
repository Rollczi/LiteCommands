
package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

public class KeyAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Key.class, (annotation, metaHolder) -> {
            metaHolder.meta().put(Meta.ARGUMENT_KEY, annotation.value());
        });
    }

}

