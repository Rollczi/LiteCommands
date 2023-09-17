package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;

import java.lang.annotation.Annotation;

class ProgrammaticExecutorStructureInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final LiteCommand<SENDER> liteCommand;
    private CommandBuilder<SENDER> commandBuilder;
    private final CommandBuilderExecutor<SENDER> executorBuilder;

    ProgrammaticExecutorStructureInvoker(LiteCommand<SENDER> liteCommand, CommandBuilder<SENDER> commandBuilder, CommandBuilderExecutor<SENDER> executorBuilder) {
        this.liteCommand = liteCommand;
        this.commandBuilder = commandBuilder;
        this.executorBuilder = executorBuilder;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onExecutorStructure(Class<A> annotationType, AnnotationProcessor.StructureExecutorListener<SENDER, A> listener) {
        A annotation = liteCommand.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        executorBuilder.setExecutorFactory((parent, requirements) -> new ProgrammaticCommandExecutor<>(parent, requirements, liteCommand));
        commandBuilder = listener.call(annotation, commandBuilder, executorBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}
