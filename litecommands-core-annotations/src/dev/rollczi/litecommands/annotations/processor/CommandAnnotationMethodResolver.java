package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface CommandAnnotationMethodResolver<SENDER, A extends Annotation> {

    CommandBuilder<SENDER> resolve(Object instance, Method method, A annotation, CommandBuilder<SENDER> context, CommandBuilderExecutor<SENDER> executorBuilder);

}
