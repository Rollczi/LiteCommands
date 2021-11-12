package dev.rollczi.litecommands.annotations.parser;

import dev.rollczi.litecommands.component.ScopeMetaData;
import panda.std.Option;

import java.lang.reflect.AnnotatedElement;

public interface AnnotationParser {

    /**
     * Annotation parser
     *
     * @throws IllegalArgumentException when annotatedElement isn't instance of Method or Class
     * @param annotatedElement instance of Method or Class
     * @return information contained in annotatedElement's annotations
     */
    Option<ScopeMetaData> parse(AnnotatedElement annotatedElement);

}