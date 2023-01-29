package dev.rollczi.litecommands.factory;

import dev.rollczi.litecommands.command.section.CommandSection;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.util.List;

public interface CommandStateFactory<SENDER> {

    Option<List<CommandSection<SENDER>>> create(Object instance);

    <A extends Annotation> void annotationResolver(FactoryAnnotationResolver<A> resolver);

    <T> void editor(Class<T> on, CommandEditor editor);

    void editor(String name, CommandEditor editor);

    void stateProcessor(CommandStateFactoryProcessor processor);

}
