package dev.rollczi.litecommands.annotations.parser;

import dev.rollczi.litecommands.annotations.IgnoreClass;
import dev.rollczi.litecommands.annotations.IgnoreMethod;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.argument.ArgumentHandler;
import panda.std.Option;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public interface AnnotationParser {

    /**
     * Annotation parser
     *
     * @param annotatedElement instance of {@link Method} or {@link Class}
     * @return optional information contained in annotatedElement's annotation;
     *      empty option if annotatedElement isn't instance of {@link Method} or {@link Class};
     *      empty option if annotatedElement contains annotation {@link IgnoreMethod} or {@link IgnoreClass}.
     */
    Option<ScopeMetaData> parse(AnnotatedElement annotatedElement);

    Set<ArgumentHandler<?>> getArgumentHandlers(Class<?> argumentClass);

    Map<Class<?>, Set<ArgumentHandler<?>>> getArgumentHandlers();

}