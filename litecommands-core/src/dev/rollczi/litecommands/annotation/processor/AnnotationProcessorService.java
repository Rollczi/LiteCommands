package dev.rollczi.litecommands.annotation.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.util.ArrayList;
import java.util.List;

public class AnnotationProcessorService<SENDER> {

    private final List<AnnotationProcessor<SENDER>> annotationProcessors = new ArrayList<>();

    public <A extends AnnotationProcessor<SENDER>> AnnotationProcessorService<SENDER> register(A processor) {
        annotationProcessors.add(processor);
        return this;
    }

    public CommandBuilder<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        for (AnnotationProcessor<SENDER> processor : annotationProcessors) {
            invoker = processor.process(invoker);
        }

        return invoker.getResult();
    }

}
