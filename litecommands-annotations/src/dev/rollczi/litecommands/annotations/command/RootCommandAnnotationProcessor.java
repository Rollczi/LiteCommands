package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;

public class RootCommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onClass(RootCommand.class, (classType, annotation, builder) -> CommandBuilder.<SENDER>createRoot()
            .applyMeta(meta -> meta.apply(builder.meta()))
        );
    }
}
