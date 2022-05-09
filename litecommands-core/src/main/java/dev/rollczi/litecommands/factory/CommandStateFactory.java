package dev.rollczi.litecommands.factory;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.command.section.CommandSection;
import panda.std.Option;

import java.lang.annotation.Annotation;

public interface CommandStateFactory {

    Option<CommandSection> create(Object instance);

    <A extends Annotation> void argument(Class<A> annotation, Class<?> on, Argument<A> argument);

    <A extends Annotation> void annotationResolver(FactoryAnnotationResolver<A> resolver);

    <T> void editor(Class<T> on, CommandEditor editor);

    void stateProcessor(CommandStateFactoryProcessor processor);

}
