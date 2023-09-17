package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;

import java.lang.annotation.Annotation;

class ProgrammaticRequirementInvoker<SENDER> implements AnnotationInvoker<SENDER> {

    private final LiteCommand<SENDER> liteCommand;
    private CommandBuilder<SENDER> commandBuilder;
    private final CommandBuilderExecutor<SENDER> executorBuilder;

    public ProgrammaticRequirementInvoker(LiteCommand<SENDER> liteCommand, CommandBuilder<SENDER> commandBuilder, CommandBuilderExecutor<SENDER> executorBuilder) {
        this.liteCommand = liteCommand;
        this.commandBuilder = commandBuilder;
        this.executorBuilder = executorBuilder;
    }

    @Override
    public <A extends Annotation> AnnotationInvoker<SENDER> onRequirement(Class<A> annotationType, AnnotationProcessor.RequirementListener<SENDER, A> listener) {
        for (AnnotationHolder<A, ?, ?> requirement : liteCommand.getRequirements(annotationType)) {
            commandBuilder = listener.call(requirement, commandBuilder, executorBuilder);
        }

        return this;
    }

    @Override
    public CommandBuilder<SENDER> getResult() {
        return commandBuilder;
    }

}

