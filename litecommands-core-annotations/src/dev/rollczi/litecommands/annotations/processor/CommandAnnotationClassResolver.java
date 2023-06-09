package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.lang.annotation.Annotation;

public interface CommandAnnotationClassResolver<SENDER, A extends Annotation> {

    CommandBuilder<SENDER> resolve(Object instance, A annotation, CommandBuilder<SENDER> context);

}
