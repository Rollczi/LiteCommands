package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;

public class RootCommandAnnotationProcessor<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onStructure(RootCommand.class, (annotation, builder) -> CommandBuilder.createRoot());
    }
}
