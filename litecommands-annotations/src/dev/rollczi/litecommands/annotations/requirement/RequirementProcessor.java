package dev.rollczi.litecommands.annotations.requirement;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;
import java.util.Optional;

@Deprecated
public abstract class RequirementProcessor<SENDER, A extends Annotation> implements AnnotationProcessor<SENDER> {

    private final Class<A> annotationClass;
    private final Class<?> parsedType;

    public RequirementProcessor(Class<A> annotationClass) {
        this.annotationClass = annotationClass;
        this.parsedType = Object.class;
    }

    public RequirementProcessor(Class<A> annotationClass, Class<?> parsedType) {
        this.annotationClass = annotationClass;
        this.parsedType = parsedType;
    }

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        try {
            return invoker.onParameter(annotationClass, (parameter, holder, builder) -> {
                TypeToken<?> typeToken = holder.getType();

                if (typeToken.isInstanceOf(parsedType)) {
                    return Optional.of(create(holder));
                }

                return Optional.empty();
            });
        } catch (LiteCommandsException liteCommandsException) {
            String errorMessage = String.format("Cannot create requirement for annotation %s with processor %s",
                annotationClass.getSimpleName(),
                this.getClass().getSimpleName()
            );

            throw new LiteCommandsException(errorMessage, liteCommandsException);
        }
    }

    abstract protected Requirement<?> create(AnnotationHolder<A, ?> holder);

}
