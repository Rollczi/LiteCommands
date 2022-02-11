package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import org.panda_lang.utilities.inject.Resources;

public interface Bind extends NativeBind {

    void bind(Resources resources);

    @Override
    default void bind(AnnotationParser annotationParser, Resources resources) {
        this.bind(resources);
    }

}
