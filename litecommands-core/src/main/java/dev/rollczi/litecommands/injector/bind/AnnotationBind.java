package dev.rollczi.litecommands.injector.bind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface AnnotationBind<T, A extends Annotation> {

    T extract(Parameter parameter, A annotation);

}
