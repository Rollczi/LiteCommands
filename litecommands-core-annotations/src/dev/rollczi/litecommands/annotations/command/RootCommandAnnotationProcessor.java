package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.processor.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;

public class RootCommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onStructure(RootCommand.class, (annotation, builder) -> CommandBuilder.createRoot());
    }
}
