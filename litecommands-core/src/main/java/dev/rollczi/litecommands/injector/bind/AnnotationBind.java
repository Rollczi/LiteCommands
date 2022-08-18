package dev.rollczi.litecommands.injector.bind;

import dev.rollczi.litecommands.command.Invocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface AnnotationBind<TYPE, SENDER, ANNOTATION extends Annotation> {

    TYPE extract(Invocation<SENDER> invocation, Parameter parameter, ANNOTATION annotation);

}
