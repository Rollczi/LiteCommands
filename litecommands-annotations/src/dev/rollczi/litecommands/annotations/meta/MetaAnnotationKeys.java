package dev.rollczi.litecommands.annotations.meta;

import dev.rollczi.litecommands.meta.MetaKey;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import org.jetbrains.annotations.ApiStatus;

/**
 * LiteCommands Annotation API
 */
@ApiStatus.Experimental
public final class MetaAnnotationKeys {

    private MetaAnnotationKeys() {
    }

    /**
     * Holds the source parameter of the annotated element.
     */
    @ApiStatus.Experimental
    public static final MetaKey<Parameter> SOURCE_PARAMETER = MetaKey.of("source-parameter", Parameter.class);

    /**
     * Holds the source annotated element of the annotated element. (Class, Method, Parameter, etc.)
     */
    @ApiStatus.Experimental
    public static final MetaKey<AnnotatedElement> SOURCE_ANNOTATED_ELEMENT = MetaKey.of("source-annotated-element", AnnotatedElement.class);

    /**
     * Holds the source annotation of the annotated element.
     */
    @ApiStatus.Experimental
    public static final MetaKey<Annotation> SOURCE_ANNOTATION = MetaKey.of("source-annotation", Annotation.class);

}
