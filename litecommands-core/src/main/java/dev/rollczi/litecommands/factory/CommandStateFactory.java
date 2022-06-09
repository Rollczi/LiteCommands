package dev.rollczi.litecommands.factory;

import dev.rollczi.litecommands.command.section.CommandSection;
import panda.std.Option;

import java.lang.annotation.Annotation;

public interface CommandStateFactory<SENDER> {

    Option<CommandSection<SENDER>> create(Object instance);

    <A extends Annotation> void annotationResolver(FactoryAnnotationResolver<A> resolver);

    <T> void editor(Class<T> on, CommandEditor editor);

    void editor(String name, CommandEditor editor);

    void stateProcessor(CommandStateFactoryProcessor processor);

}
