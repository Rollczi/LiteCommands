package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.annotation.processor.SourceProcessor;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessorService;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;

class ProgrammaticSourceProcessor<SENDER> implements SourceProcessor<SENDER, LiteCommand<SENDER>> {

    private final AnnotationProcessorService<SENDER> annotationProcessorService;

    public ProgrammaticSourceProcessor(
        AnnotationProcessorService<SENDER> annotationProcessorService) {
        this.annotationProcessorService = annotationProcessorService;
    }

    @Override
    public CommandBuilder<SENDER> processBuilder(LiteCommand<SENDER> source) {
        CommandBuilder<SENDER> context = CommandBuilder.create();

        ProgrammaticStructureInvoker<SENDER> classInvoker = new ProgrammaticStructureInvoker<>(source, context);
        context = annotationProcessorService.process(classInvoker);

        CommandBuilderExecutor<SENDER> executorBuilder = new CommandBuilderExecutor<>(context);
        ProgrammaticExecutorStructureInvoker<SENDER> methodInvoker = new ProgrammaticExecutorStructureInvoker<>(source, context, executorBuilder);

        context = annotationProcessorService.process(methodInvoker);

        if (!executorBuilder.buildable()) {
            return context;
        }

        ProgrammaticRequirementInvoker<SENDER> requirementInvoker = new ProgrammaticRequirementInvoker<>(source, context, executorBuilder);
        context = annotationProcessorService.process(requirementInvoker); //TODO zrobić return value flow

        return context;
    }

}
