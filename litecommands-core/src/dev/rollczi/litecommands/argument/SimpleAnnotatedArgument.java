package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;

public class SimpleAnnotatedArgument<T> extends SimpleArgument<T> {

    private final Map<Class<? extends Annotation>, Annotation> annotationMap;

    public SimpleAnnotatedArgument(String name, WrapFormat<T, ?> wrapperFormat, Map<Class<? extends Annotation>, Annotation> annotationMap) {
        super(name, wrapperFormat);
        this.annotationMap = annotationMap;
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotationMap.values().toArray(new Annotation[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Optional<A> getAnnotation(Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable((A) annotationMap.get(annotationClass));
    }

}
