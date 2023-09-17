package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.lang.annotation.Annotation;

class ProgrammaticStructureInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final LiteCommand<SENDER> liteCommand;
    private CommandBuilder<SENDER> commandBuilder;

    public ProgrammaticStructureInvoker(LiteCommand<SENDER> liteCommand, CommandBuilder<SENDER> commandBuilder) {
        this.liteCommand = liteCommand;
        this.commandBuilder = commandBuilder;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> on(Class<A> annotationType, AnnotationProcessor.Listener<A> listener) {
        A annotation = liteCommand.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        listener.call(annotation, commandBuilder);
        return this;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onStructure(Class<A> annotationType, AnnotationProcessor.StructureListener<SENDER, A> listener) {
        A annotation = liteCommand.getAnnotation(annotationType);

        if (annotation == null) {
            return this;
        }

        commandBuilder = listener.call(annotation, commandBuilder);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}
