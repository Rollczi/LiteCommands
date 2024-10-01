
package dev.rollczi.litecommands.annotations.quoted;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.quoted.QuotedStringArgumentResolver;

public class QuotedAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.on(Quoted.class, (annotation, metaHolder) -> {
            metaHolder.meta().edit(Meta.ARGUMENT_KEY, argumentKey -> argumentKey.profiled(QuotedStringArgumentResolver.KEY));
        });
    }

}

