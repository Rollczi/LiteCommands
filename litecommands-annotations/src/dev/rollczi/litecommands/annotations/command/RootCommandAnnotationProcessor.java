package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.strict.StrictMode;

public class RootCommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker
            .on(RootCommand.class, (annotation, metaHolder) -> {
                if (annotation.strict() != StrictMode.DEFAULT) {
                    metaHolder.meta().put(Meta.STRICT_MODE, annotation.strict());
                }
            })
            .onClass(RootCommand.class, (classType, annotation, builder) -> CommandBuilder.<SENDER>createRoot()
                .applyMeta(meta -> meta.putAll(builder.meta()))
            );
    }
}
