package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.annotation.AnnotationHolder;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class RequirementFactoryRegistry<SENDER> {

    private final Map<Class<? extends Annotation>, RequirementFactory<SENDER, ?>> annotations = new HashMap<>();

    public <A extends Annotation> void registerFactory(Class<A> type, RequirementFactory<SENDER, A> argumentFactory) {
        annotations.put(type, argumentFactory);
    }

    @SuppressWarnings("unchecked")
    public <T, A extends Annotation> Requirement<SENDER, T> create(AnnotationHolder<A, T, ?> holder) {
        A annotation = holder.getAnnotation();
        RequirementFactory<SENDER, A> factory = (RequirementFactory<SENDER, A>) annotations.get(annotation.annotationType());

        if (factory == null) {
            throw new IllegalArgumentException("Argument factory for annotation " + annotation.annotationType().getName() + " not found");
        }

        return factory.create(holder);
    }

}
