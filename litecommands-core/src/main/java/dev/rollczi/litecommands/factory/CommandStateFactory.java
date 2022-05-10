package dev.rollczi.litecommands.factory;

import dev.rollczi.litecommands.command.section.CommandSection;
import panda.std.Option;

import java.lang.annotation.Annotation;

public interface CommandStateFactory {

    Option<CommandSection> create(Object instance);

    <A extends Annotation> void annotationResolver(FactoryAnnotationResolver<A> resolver);

    <T> void editor(Class<T> on, CommandEditor editor);

    void stateProcessor(CommandStateFactoryProcessor processor);

}
